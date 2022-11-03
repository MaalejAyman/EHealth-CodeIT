<?php

namespace App\Security;

use App\Entity\User;
use Doctrine\ORM\EntityManagerInterface;
use Symfony\Component\HttpFoundation\RedirectResponse;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\Routing\Generator\UrlGeneratorInterface;
use Symfony\Component\Security\Core\Authentication\Token\TokenInterface;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Core\Exception\CustomUserMessageAuthenticationException;
use Symfony\Component\Security\Core\Exception\InvalidCsrfTokenException;
use Symfony\Component\Security\Core\Security;
use Symfony\Component\Security\Core\User\UserInterface;
use Symfony\Component\Security\Core\User\UserProviderInterface;
use Symfony\Component\Security\Csrf\CsrfToken;
use Symfony\Component\Security\Csrf\CsrfTokenManagerInterface;
use Symfony\Component\Security\Guard\Authenticator\AbstractFormLoginAuthenticator;
use Symfony\Component\Security\Guard\PasswordAuthenticatedInterface;
use Symfony\Component\Security\Http\Util\TargetPathTrait;

class AppCustomAuthenticator extends AbstractFormLoginAuthenticator implements PasswordAuthenticatedInterface
{
    use TargetPathTrait;

    public const LOGIN_ROUTE = 'app_login';

    private $entityManager;
    private $urlGenerator;
    private $csrfTokenManager;
    private $passwordEncoder;

    public function __construct(EntityManagerInterface $entityManager, UrlGeneratorInterface $urlGenerator, CsrfTokenManagerInterface $csrfTokenManager, UserPasswordEncoderInterface $passwordEncoder)
    {
        $this->entityManager = $entityManager;
        $this->urlGenerator = $urlGenerator;
        $this->csrfTokenManager = $csrfTokenManager;
        $this->passwordEncoder = $passwordEncoder;
    }

    public function supports(Request $request)
    {
        return self::LOGIN_ROUTE === $request->attributes->get('_route')
            && $request->isMethod('POST');
    }

    public function getCredentials(Request $request)
    {
        $credentials = [
            'email' => $request->request->get('email'),
            'password' => $request->request->get('password'),
            'csrf_token' => $request->request->get('_csrf_token'),
            'recaptcha' => $request->request->get('g-recaptcha-response'),
        ];
        $request->getSession()->set(
            Security::LAST_USERNAME,
            $credentials['email']
        );

        return $credentials;
    }

    public function getUser($credentials, UserProviderInterface $userProvider)
    {
        $secret = "6LfU0Y8aAAAAAChMM0r2N0zgC3SL3jBiJ_-oOp4L";
        $recaptcha = new \ReCaptcha\ReCaptcha($secret);
        $resp = $recaptcha->setExpectedHostname('127.0.0.1')->verify($credentials['recaptcha']);
        $valid_captch = $resp->isSuccess();
        $token = new CsrfToken('authenticate', $credentials['csrf_token']);
        if (!$this->csrfTokenManager->isTokenValid($token)) {
            throw new InvalidCsrfTokenException();
        }
        $user = $this->entityManager->getRepository(User::class)->findOneBy(['email' => $credentials['email']]);

        if (!$user) {

            // fail authentication with a custom error
            throw new CustomUserMessageAuthenticationException('Email n\'existe pas.');

        }
        if(!$valid_captch){
            throw new CustomUserMessageAuthenticationException('verifier que vous n\'etes pas un rebot');

        }
        return $user;
    }

    public function checkCredentials($credentials, UserInterface $user)
    {
        return $this->passwordEncoder->isPasswordValid($user, $credentials['password']);
    }

    /**
     * Used to upgrade (rehash) the user's password automatically over time.
     */
    public function getPassword($credentials): ?string
    {
        return $credentials['password'];
    }

    public function onAuthenticationSuccess(Request $request, TokenInterface $token, $providerKey)
    {
        /*if ($targetPath = $this->getTargetPath($request->getSession(), $providerKey)) {
            return new RedirectResponse($targetPath);
        }*/
        // For example : return new RedirectResponse($this->urlGenerator->generate('some_route'));
        //throw new \Exception('TODO: provide a valid redirect inside '.__FILE__);
        {
            // Get list of roles for current user
            $roles = $token->getRoles();
            // Tranform this list in array
            $rolesTab = array_map(function($role){
                return $role->getRole();
            }, $roles);
            // If is a admin or super admin we redirect to the backoffice area
            if (in_array('ROLE_ADMIN', $rolesTab, true))
                $redirection = new RedirectResponse($this->urlGenerator->generate('acceuil'));
            // otherwise, if is a commercial user we redirect to the crm area
            elseif (in_array('ROLE_MEDECIN', $rolesTab, true))
                $redirection = new RedirectResponse($this->urlGenerator->generate('pageAccMed'));
            // otherwise we redirect user to the member area
            elseif (in_array('ROLE_LABORATOIRE', $rolesTab, true))
                $redirection = new RedirectResponse($this->urlGenerator->generate('pageAccLab'));
            // otherwise we redirect user to the member area
            elseif (in_array('ROLE_USER', $rolesTab, true))
                $redirection = new RedirectResponse($this->urlGenerator->generate('pageAcc'));
            // otherwise we redirect user to the member area

            else
                $redirection = new RedirectResponse($this->router->generate('app_login'));

            return $redirection;
        }
    }


    protected function getLoginUrl()
    {
        return $this->urlGenerator->generate(self::LOGIN_ROUTE);
    }
}
