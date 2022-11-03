<?php

namespace App\Controller;

use App\Entity\Laboratoire;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\User;
use App\Form\LaboratoireType;
use App\Form\MedecinType;
use App\Form\RegistrationFormType;
use App\Security\EmailVerifier;
use App\Security\AppCustomAuthenticator;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Security\Guard\GuardAuthenticatorHandler;
use SymfonyCasts\Bundle\VerifyEmail\Exception\VerifyEmailExceptionInterface;

class RegistrationController extends AbstractController
{
    private $emailVerifier;

    public function __construct(EmailVerifier $emailVerifier)
    {
        $this->emailVerifier = $emailVerifier;
    }

    /**
     * @Route("/register", name="app_register")
     */
    public function register(Request $request, UserPasswordEncoderInterface $passwordEncoder, GuardAuthenticatorHandler $guardHandler, AppCustomAuthenticator $authenticator): Response
    {
       // $user = new User();
        $patient=new Patient();

        $form = $this->createForm(RegistrationFormType::class, $patient);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $patient->setPassword(
                $passwordEncoder->encodePassword(
                    $patient,
                    $form->get('plainPassword')->getData()
                )
            );
            $patient->setRoles(["ROLE_USER"]);
            $entityManager = $this->getDoctrine()->getManager();
            //$p=new Patient();
           // $p->setSexe($form->get('sexe')->getData());
            $entityManager->persist($patient);
            $entityManager->flush();

            // generate a signed url and email it to the user
            $this->emailVerifier->sendEmailConfirmation('app_verify_email', $patient,
                (new TemplatedEmail())
                    ->from(new Address('meriamglaa0@gmail.com', 'EHealth'))
                    ->to($patient->getEmail())
                    ->subject('Vérifier votre compte')
                    ->htmlTemplate('registration/confirmation_email.html.twig')
            );
            // do anything else you need here, like send an email
            return $guardHandler->authenticateUserAndHandleSuccess(
                $patient,
                $request,
                $authenticator,
                'main' // firewall name in security.yaml
            );
        }

        return $this->render('registration/register.html.twig', [
            'registrationForm' => $form->createView(),
        ]);
    }

    /**
     * @Route("/verify/email", name="app_verify_email")
     */
    public function verifyUserEmail(Request $request): Response
    {
        $this->denyAccessUnlessGranted('IS_AUTHENTICATED_FULLY');

        // validate email confirmation link, sets User::isVerified=true and persists
        try {
            $this->emailVerifier->handleEmailConfirmation($request, $this->getUser());
        } catch (VerifyEmailExceptionInterface $exception) {
            $this->addFlash('verify_email_error', $exception->getReason());

            return $this->redirectToRoute('app_register');
        }

        // @TODO Change the redirect on success and handle or remove the flash message in your templates
        $this->addFlash('success', 'Your email address has been verified.');

        return $this->redirectToRoute('app_register');
    }




    /**
     * @Route("/registerLab", name="app_register_lab")
     */
    public function registerLab(Request $request, UserPasswordEncoderInterface $passwordEncoder, GuardAuthenticatorHandler $guardHandler, AppCustomAuthenticator $authenticator): Response
    {
        // $user = new User();
        $laboratoire=new Laboratoire();
        $form = $this->createForm(LaboratoireType::class, $laboratoire);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $laboratoire->setPassword(
                $passwordEncoder->encodePassword(
                    $laboratoire,
                    $form->get('plainPassword')->getData()
                )
            );
            $laboratoire->setRoles(["ROLE_LABORATOIRE"]);
            $entityManager = $this->getDoctrine()->getManager();
            //$p=new Patient();
            // $p->setSexe($form->get('sexe')->getData());
            $entityManager->persist($laboratoire);
            $entityManager->flush();

            // generate a signed url and email it to the user
            $this->emailVerifier->sendEmailConfirmation('app_verify_email', $laboratoire,
                (new TemplatedEmail())
                    ->from(new Address('meriamglaa0@gmail.com', 'EHealth'))
                    ->to($laboratoire->getEmail())
                    ->subject('Vérifier votre compte')
                    ->htmlTemplate('registration/confirmation_email.html.twig')
            );
            // do anything else you need here, like send an email
            return $guardHandler->authenticateUserAndHandleSuccess(
                $laboratoire,
                $request,
                $authenticator,
                'main' // firewall name in security.yaml
            );
        }

        return $this->render('tempClient/Laboratoire/registerLab.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/registerMed", name="app_register_med")
     */
    public function registerMed(Request $request, UserPasswordEncoderInterface $passwordEncoder, GuardAuthenticatorHandler $guardHandler, AppCustomAuthenticator $authenticator): Response
    {
        $medecin =new Medecin();
        $form = $this->createForm(MedecinType::class, $medecin);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            // encode the plain password
            $medecin->setPassword(
                $passwordEncoder->encodePassword(
                    $medecin,
                    $form->get('plainPassword')->getData()
                )
            );
            $medecin->setRoles(["ROLE_MEDECIN"]);
            $entityManager = $this->getDoctrine()->getManager();
            //$p=new Patient();
            // $p->setSexe($form->get('sexe')->getData());
            $entityManager->persist($medecin);
            $entityManager->flush();

            // generate a signed url and email it to the user
            $this->emailVerifier->sendEmailConfirmation('app_verify_email', $medecin,
                (new TemplatedEmail())
                    ->from(new Address('meriamglaa0@gmail.com', 'EHealth'))
                    ->to($medecin->getEmail())
                    ->subject('Vérifier votre compte')
                    ->htmlTemplate('registration/confirmation_email.html.twig')
            );
            // do anything else you need here, like send an email
            return $guardHandler->authenticateUserAndHandleSuccess(
                $medecin,
                $request,
                $authenticator,
                'main' // firewall name in security.yaml
            );
        }

        return $this->render('tempClient/Medecin/registerMed.html.twig', [
            'form' => $form->createView(),
        ]);
    }

}
