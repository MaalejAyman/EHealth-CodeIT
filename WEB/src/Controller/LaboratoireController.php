<?php

namespace App\Controller;

use App\Entity\DossierLab;
use App\Entity\Event;
use App\Entity\Laboratoire;
use App\Entity\Maladie;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use App\Form\ChangerLaboratoireType;
use App\Form\CreaterendezvousType;
use App\Form\EventType;
use App\Form\LaboratoireType;
use App\Repository\DossierLabRepository;
use App\Repository\LaboratoireRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Mime\Address;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\DateTimeNormalizer;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;


class LaboratoireController extends AbstractController
{
    /**
     * @Route("/Admin/Laboratoire", name="laboratoire_index", methods={"GET"})
     */
    public function index(LaboratoireRepository $laboratoireRepository,PaginatorInterface $paginator,Request $request): Response
    {
        $donnes =$laboratoireRepository->OrderByMailQB();
        $orderedList=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            9
        );
        return $this->render('tempAdmin/Laboratoire/index.html.twig', [
            'controller_name' => 'LaboratoireController',
            'laboratoires' => $orderedList
        ]);
    }
	/**
     * @Route("/Laboratoire/pageAccLab", name="pageAccLab")
     */
    public function pageAcc(): Response
    {
        $data = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        return $this->render('tempClient/Laboratoire/mainLab.html.twig', [
            'controller_name' => 'LaboratoireController','data'=>$data
        ]);
    }
    /**
     * @Route("/Laboratoire/profil", name="profil")
     */
    public function profil(): Response
    {
        $data = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        return $this->render('tempClient/Laboratoire/profilLab.html.twig', [
            'controller_name' => 'LaboratoireController','data'=>$data
        ]);
    }
    /**
     * @Route("/Laboratoire/new", name="laboratoire_new", methods={"GET","POST"})
     */
    public function new(Request $request,UserPasswordEncoderInterface $encoder,MailerInterface $mailer): Response
    {
        $laboratoire = new Laboratoire();
        $form = $this->createForm(LaboratoireType::class, $laboratoire);
        $form->remove('agreeTerms');
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $pass=$form['plainPassword']->getData();
            $laboratoire->setIsVerified(true);
			$hash= $encoder->encodePassword($laboratoire,$form['plainPassword']->getData());																		
            $laboratoire->setPassword($hash);
            $laboratoire->setRoles(["ROLE_LABORATOIRE"]);
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($laboratoire);
           $email = (new TemplatedEmail())
                ->from('meriamglaa0@gmail.com')
                ->to($laboratoire->getEmail())
                ->subject('Nouveau Compte EHealth')
                ->htmlTemplate('registration/email.html.twig')
                ->context([
                    'lab' => $laboratoire,
                    'pass'=> $pass
                ]);
            $mailer->send($email);
            $entityManager->flush();

            return $this->redirectToRoute('laboratoire_index');
        }

        return $this->render('tempAdmin/Laboratoire/new.html.twig', [
            'Laboratoire' => $laboratoire,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Laboratoire/{id}", name="laboratoire_show", methods={"GET"})
     */
    public function show(Laboratoire $laboratoire): Response
    {
        return $this->render('tempAdmin/Laboratoire/show.html.twig', [
            'Laboratoire' => $laboratoire,
        ]);
    }

    /**
     * @Route("/Laboratoire/{id}/edit", name="laboratoire_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Laboratoire $laboratoire): Response
    {
        $form = $this->createForm(LaboratoireType::class, $laboratoire);
        $form->handleRequest($request);
        $form->remove('agreeTerms');


        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('laboratoire_index');
        }

        return $this->render('tempAdmin/Laboratoire/edit.html.twig', [
            'Laboratoire' => $laboratoire,
            'form' => $form->createView(),
        ]);
    }
    /**
     * @Route("/Laboratoire/{id}/editL", name="laboratoire_editL", methods={"GET","POST"})
     */
    public function editL(Request $request, Laboratoire $laboratoire): Response
    {
        $form = $this->createForm(LaboratoireType::class, $laboratoire);
        $form->remove('plainPassword');
        $form->remove('agreeTerms');
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();
            return $this->redirectToRoute('laboratoire_index');
        }
        return $this->render('tempAdmin/Laboratoire/editLab.html.twig', [
            'Laboratoire' => $laboratoire,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Laboratoire/{id}", name="laboratoire_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Laboratoire $laboratoire): Response
    {
        if ($this->isCsrfTokenValid('delete'.$laboratoire->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($laboratoire);
            $entityManager->flush();
        }
        return $this->redirectToRoute('laboratoire_index');
    }
	/**
     * @Route("/Laboratoire/updateLabo/u", name="updateLabo")
     */
    public function update(Request $request)
    {
        $labo = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $form = $this->createForm(LaboratoireType::class,$labo);
        $form->remove('plainPassword');
        $form->remove('agreeTerms');
        $form->remove('num_service');
        $form->remove('nom');
        $form->remove('agreeTerms');
        $form->handleRequest($request);
        if($form->isSubmitted() && ($form->isValid())){
            $em =$this->getDoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('pageAccLab');
        }else{
            return $this->render('tempClient/Laboratoire/EditerLaboratoire.html.twig',['form'=> $form->createView()]);
        }
    }

    /**
     * @Route("/Laboratoire/Services/x", name="Services", methods={"GET"})
     */
    public function showService(): Response
    {
        $laboratoire = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        return $this->render('tempClient/Laboratoire/AfficherServices.html.twig', [
            'Laboratoire' => $laboratoire,
        ]);
    }

    /**
     * @Route("/Laboratoire/updateServices/u", name="updateServices")
     */
    public function updateService(Request $request)
    {
        $labo = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $form = $this->createForm(LaboratoireType::class,$labo);
        $form->remove('email');
        $form->remove('Adresse');
        $form->remove('Ville');
        $form->remove('Tel');
        $form->remove('plainPassword');
        $form->remove('horaire_debut');
        $form->remove('horaire_fin');
        $form->remove('num_service');
        $form->remove('nom');
        $form->remove('agreeTerms');
        $form->handleRequest($request);
        if($form->isSubmitted() && ($form->isValid())){
            $em =$this->getDoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('pageAccLab');
        }else{
            return $this->render('tempClient/Laboratoire/AjouterServices.html.twig',['form'=> $form->createView()]);
        }
    }

    /**
     * @Route("/Laboratoire/DossierLab/x", name="dossier_lab_show", methods={"GET"})
     */
    public function indexDossier(): Response
    {
        $laboratoire = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $dossier = $this->getDoctrine()->getRepository(DossierLab::class)->findBy( ['Laboratoire' => $laboratoire ]);
        return $this->render('/tempClient/dossier_lab/show.html.twig', [
            'dossier' => $dossier,
        ]);
    }

    /**
     * @Route("/Laboratoire/Calendrier/show", name="CalendarLab")
     */
    public function CalendarLab(Request $request):Response
    {
        $lab=$this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $event = new Event();
        $form = $this->createForm(EventType::class, $event);
        $events = $this->getDoctrine()->getRepository(Event::class)->findBy(['laboratoire' => $lab]);
        $encoders = [new XmlEncoder(), new JsonEncoder()];
        $normalizers = [new ObjectNormalizer()];
        $norm = [new DateTimeNormalizer()];
        $normalizers[0]->setCircularReferenceHandler(function ($object) {
            return $object->getLaboratoire();
        });
        $serializer = new Serializer($normalizers, $encoders);
        $serializer1 = new Serializer($norm, $encoders);
        $dateend = [];
        $datestart = [];
        $rdv = [];
        foreach ($events as $ev) {
            $datestart[$ev->getId()] = $ev->getStart();
        }
        foreach ($events as $ev) {
            $dateend[$ev->getId()] = $ev->getEnd();
        }
        foreach ($events as $ev) {
            $rdv[$ev->getId()] = $ev->getRendezvous();
        }
        $jsonContent = $serializer->serialize([$events], 'json', ['ignored_attributes' => [
            'laboratoire', 'start', 'end', 'rendezvous'
        ]]);
        $jsonContent2 = $serializer->serialize([$rdv], 'json', ['ignored_attributes' => [
            'laboratoire'
        ]]);
        $data = json_encode($jsonContent);
        $dataRDV = json_encode($jsonContent2);
        $dateE = json_encode($serializer1->serialize($dateend, 'json'));
        $dateS = json_encode($serializer1->serialize($datestart, 'json'));
        $form->add('ajouter', SubmitType::class, ['label' => 'Prendre Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getDoctrine()->getManager();
            $event->setLaboratoire($lab);
            $em->persist($event);
            $em->flush();
            return $this->redirectToRoute('CalendarLab');
        } else {
            return $this->render('tempClient/Laboratoire/Calendrier.html.twig', ['form' => $form->createView(), 'data' => $data, 'dateE' => $dateE, 'dateS' => $dateS,'RDV'=>$dataRDV, 'medecin' => $lab]);
        }
    }
}
