<?php

namespace App\Controller;

use App\Entity\Laboratoire;
use App\Entity\Maladie;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Form\PatientType;
use App\Form\RechercheType;
use App\Repository\LaboratoireRepository;
use App\Repository\MedecinRepository;
use App\Repository\PatientRepository;
use App\Repository\UserRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;


class PatientController extends AbstractController
{
    /**
     * @Route("/Admin/Patient/", name="patient_index", methods={"GET"})
     */
    public function index(PatientRepository $patientRepository,PaginatorInterface $paginator,Request $request): Response
    {
        $donnes =$orderedList=$patientRepository->OrderByMailQB();
        $orderedList=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            9
        );
        return $this->render('tempAdmin/Patient/index.html.twig', [
            'controller_name' => 'PatientController',
            'patients' => $orderedList
        ]);
    }


    /**
     * @Route("/Patient/about", name="about")
     */
    public function about(): Response
    {
        return $this->render('tempClient/about.html.twig');

    }


    /**
         * @Route("/Patient/pageAcc", name="pageAcc")
     */
    public function pageAcc(): Response
    {
        $form=$this->createForm(RechercheType::class);
        return $this->render('tempClient/Patient/main.html.twig',['form'=>$form->createView(),'res'=>null]);

    }
    /**
     * @Route("/", name="pageAcceuil")
     */
    public function pageAcceuil(): Response
    {
        return $this->render('tempClient/pageAcceuil.html.twig');

    }

    /**
     * @Route("/Admin/Patient/new", name="patient_new", methods={"GET","POST"})
     */
    public function new(Request $request,UserPasswordEncoderInterface $encoder): Response
    {
        $patient = new Patient();
        $form = $this->createForm(PatientType::class, $patient);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $hash=$encoder->encodePassword($patient,$form['plainPassword']->getData());
            $patient->setPassword($hash);
            $patient->setRoles(["ROLE_USER"]);
            $patient->setIsVerified(true);
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($patient);
            $entityManager->flush();
            /*$this->emailVerifier->sendEmailConfirmation('app_verify_email', $patient,
                (new TemplatedEmail())
                    ->from(new Address('meriamglaa0@gmail.com', 'EHealth'))
                    ->to($patient->getEmail())
                    ->subject('Vérifier votre compte')
                    ->htmlTemplate('registration/confirmation_email.html.twig')
            );*/

            return $this->redirectToRoute('patient_index');
        }

        return $this->render('tempAdmin/Patient/new.html.twig', [
            'Patient' => $patient,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/Patient/{id}", name="patient_show", methods={"GET"})
     */
    public function show(Patient $patient): Response
    {
        return $this->render('tempAdmin/Patient/show.html.twig', [
            'Patient' => $patient,
        ]);
    }

    /**
     * @Route("/Admin/Patient/{id}/edit", name="patient_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Patient $patient): Response
    {
        $form = $this->createForm(PatientType::class, $patient);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('patient_index');
        }

        return $this->render('tempAdmin/Patient/edit.html.twig', [
            'Patient' => $patient,
            'form' => $form->createView(),
        ]);
    }
    /**
     * @Route("/Patient/{id}/editProfile", name="profile_edit", methods={"GET","POST"})
     */
    public function editPatient(Request $request, Patient $patient): Response
    {
        $form = $this->createForm(PatientType::class, $patient);
        $form->remove('plainPassword');
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('pageAcc');
        }

        return $this->render('tempClient/Patient/editPatient.html.twig', [
            'Patient' => $patient,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Patient/{id}", name="patient_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Patient $patient): Response
    {
        if ($this->isCsrfTokenValid('delete'.$patient->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($patient);
            $entityManager->flush();
        }

        return $this->redirectToRoute('patient_index');
    }
    /**
     * @Route("/Patient/{id}/P", name="patient_deleteP", methods={"DELETE"})
     */
    public function deleteP(Request $request, Patient $patient): Response
    {
        if ($this->isCsrfTokenValid('delete'.$patient->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($patient);
            $entityManager->flush();
        }

        return $this->redirectToRoute('pageAcc');
    }

    /**
     * @Route("/Patient/Rech/rech", name="Rechercher")
     * @param Request $request
     * @param UserRepository $userRepository
     * @return Response
     */
    public function Rechercher(Request $request,UserRepository $userRepository):Response
    {
        $user = $this->getUser();
        if (!$user) {
            return $this->json(['code' => 403, 'error' => 'Vous devez être connecté !'], 403);
        }
        $data = $request->getContent();
        $data = json_decode($data, true);
        if($data['Type'] == 'Laboratoire')
        {
            $res = $userRepository->findByBothFieldsLab($data['Ville'],$data['Service'],$data['Recherche']);
            $encoders = [new XmlEncoder(), new JsonEncoder()];
            $normalizers = [new ObjectNormalizer()];
            $normalizers[0]->setCircularReferenceHandler(function ($object) {
                return $object->getRendezVous();
            });
            $serializer = new Serializer($normalizers, $encoders);

            $jsonContent = $serializer->serialize([$res], 'json', ['ignored_attributes' => [
                'rendezvous'
            ]]);
        }
        elseif($data['Type'] == 'Medecin')
        {
            $res = $userRepository->findByBothFieldsMed($data['Ville'],$data['Spec'],$data['Recherche']);
            $encoders = [new XmlEncoder(), new JsonEncoder()];
            $normalizers = [new ObjectNormalizer()];
            $normalizers[0]->setCircularReferenceHandler(function ($object) {
                return $object->getRendezVous();
            });
            $serializer = new Serializer($normalizers, $encoders);

            $jsonContent = $serializer->serialize([$res], 'json', ['ignored_attributes' => [
                'rendezvous'
            ]]);
        }else{
            $res1 = $userRepository->findByBothFieldsMed($data['Ville'],$data['Spec'],$data['Recherche']);
            $res2 = $userRepository->findByBothFieldsLab($data['Ville'],$data['Service'],$data['Recherche']);
            $encoders = [new XmlEncoder(), new JsonEncoder()];
            $normalizers = [new ObjectNormalizer()];
            $normalizers[0]->setCircularReferenceHandler(function ($object) {
                return $object->getRendezVous();
            });
            $serializer = new Serializer($normalizers, $encoders);

            $jsonContent = $serializer->serialize([$res1,$res2], 'json', ['ignored_attributes' => [
                'rendezvous'
            ]]);
        }
        $response = new Response(json_encode($jsonContent));
        $response->headers->set('Content-Type', 'application/json; charset=utf-8');
        return $response;
    }
    /**
     * @Route("/Patient/ConsulterLaboratoire/{id}", name="ConsulterLaboratoire")
     */
    public function ConsulterLaboratoire(Laboratoire $laboratoire): Response
    {
        $em = $this->getDoctrine()->getManager();
        $p = $em->getRepository(Patient::class)->find($this->getUser());
        return $this->render('tempClient/Patient/ConsulterLaboratoire.html.twig', [
            'laboratoire' => $laboratoire,'patient'=>$p
        ]);
    }
    /**
     * @Route("/Patient/ConsulterMedecin/{id}", name="ConsulterMedecin")
     */
    public function ConsulterMedecin(Medecin $medecin): Response
    {
        $em = $this->getDoctrine()->getManager();
        $p = $em->getRepository(Patient::class)->find($this->getUser());
        return $this->render('tempClient/Patient/ConsulterMedecin.html.twig', [
            'medecin' => $medecin,'patient'=>$p
        ]);
    }
    /**
     * @Route("/Admin/{id}", name="patient_block", methods={"BLOCK"})
     */
    public function block(Request $request, Patient $patient): Response
    {
        $em = $this->getDoctrine()->getManager();
        $p = $em->getRepository(Patient::class)->find($patient);
        $p->setBlocked(1);
        $em->persist($p);
        $em->flush();
        return $this->redirectToRoute('patient_index');
    }

    /**
     * @Route("/Admin/{id}", name="patient_unblock", methods={"UNBLOCK"})
     */
    public function unblock(Request $request, Patient $patient): Response
    {
        $em = $this->getDoctrine()->getManager();
        $p = $em->getRepository(Patient::class)->find($patient);
        $p->setBlocked(0);
        $em->persist($p);
        $em->flush();
        return $this->redirectToRoute('patient_index');
    }

}
