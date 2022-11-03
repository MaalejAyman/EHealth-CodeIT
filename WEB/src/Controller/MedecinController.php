<?php

namespace App\Controller;

use App\Entity\Event;
use App\Entity\Medecin;
use App\Form\EventType;
use App\Form\MedecinType;
use App\Repository\MedecinRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bridge\Twig\Mime\TemplatedEmail;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Mailer\MailerInterface;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Core\Encoder\UserPasswordEncoderInterface;
use Symfony\Component\Mime\Email;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\DateTimeNormalizer;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;


class MedecinController extends AbstractController
{
    /**
     * @Route("/Admin/Medecin/", name="medecin_index", methods={"GET"})
     */
    public function index(MedecinRepository $medecinRepository,PaginatorInterface $paginator,Request $request): Response
    {
        $donnes =$orderedList=$medecinRepository->OrderByMailQB();
        $orderedList=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            12
        );
        return $this->render('tempAdmin/Medecin/index.html.twig', [
            'controller_name' => 'MedecinController',
            'medecins' => $orderedList
        ]);
    }
    /**
     * @Route("/Medecin/pageAccMed", name="pageAccMed")
     */
    public function pageAcc(): Response
    {
        return $this->render('tempClient/Medecin/mainMed.html.twig');

    }
    /**
     * @Route("/Admin/Medecin/new", name="medecin_new", methods={"GET","POST"})
     */
    public function new(Request $request,UserPasswordEncoderInterface $encoder,MailerInterface $mailer): Response
    {
        $medecin = new Medecin();
        $form = $this->createForm(MedecinType::class, $medecin);
        $form->remove('agreeTerms');
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $pass=$form['plainPassword']->getData();
            $hash=$encoder->encodePassword($medecin,$form['plainPassword']->getData());
            $medecin->setPassword($hash);
			$medecin->setIsVerified(true);
            $medecin->setRoles(["ROLE_MEDECIN"]);
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($medecin);
            $email = (new TemplatedEmail())
                ->from('meriamglaa0@gmail.com')
                ->to($medecin->getEmail())
                ->subject('Nouveau Compte EHealth')
                ->htmlTemplate('registration/emailMed.html.twig')
                ->context([
                    'med' => $medecin,
                    'pass'=> $pass
                ]);

            $mailer->send($email);
            $entityManager->flush();

            return $this->redirectToRoute('medecin_index');
        }

        return $this->render('tempAdmin/Medecin/new.html.twig', [
            'Medecin' => $medecin,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/Medecin/{id}", name="medecin_show", methods={"GET"})
     */
    public function show(Medecin $medecin): Response
    {
        return $this->render('tempAdmin/Medecin/show.html.twig', [
            'Medecin' => $medecin,
        ]);
    }

    /**
     * @Route("/Admin/Medecin/{id}/edit", name="medecin_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Medecin $medecin): Response
    {
        $form = $this->createForm(MedecinType::class, $medecin);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('medecin_index');
        }

        return $this->render('tempAdmin/Medecin/edit.html.twig', [
            'Medecin' => $medecin,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/Medecin/{id}/editM", name="medecin_editM", methods={"GET","POST"})
     */
    public function editM(Request $request, Medecin $medecin): Response
    {
        $form = $this->createForm(MedecinType::class, $medecin);
        $form->remove('plainPassword');
        $form->remove('agreeTerms');
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('medecin_index');
        }

        return $this->render('tempAdmin/Medecin/editMed.html.twig', [
            'Medecin' => $medecin,
            'form' => $form->createView(),
        ]);
    }
    /**
     * @Route("/Admin/Medecin/{id}", name="medecin_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Medecin $medecin): Response
    {
        if ($this->isCsrfTokenValid('delete'.$medecin->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($medecin);
            $entityManager->flush();
        }

        return $this->redirectToRoute('medecin_index');
    }
    /**
     * @Route("/Medecin/Calendrier/show", name="CalendarMed")
     */
    public function CalendarMed(Request $request):Response
    {
        $lab=$this->getDoctrine()->getRepository(Medecin::class)->find($this->getUser()->getId());
        $event = new Event();
        $form = $this->createForm(EventType::class, $event);
        $events = $this->getDoctrine()->getRepository(Event::class)->findBy(['medecin' => $lab]);
        $encoders = [new XmlEncoder(), new JsonEncoder()];
        $normalizers = [new ObjectNormalizer()];
        $norm = [new DateTimeNormalizer()];
        $normalizers[0]->setCircularReferenceHandler(function ($object) {
            return $object->getMedecin();
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
            'medecin', 'start', 'end', 'rendezvous'
        ]]);
        $jsonContent2 = $serializer->serialize([$rdv], 'json', ['ignored_attributes' => [
            'medecin'
        ]]);
        $data = json_encode($jsonContent);
        $dataRDV = json_encode($jsonContent2);
        $dateE = json_encode($serializer1->serialize($dateend, 'json'));
        $dateS = json_encode($serializer1->serialize($datestart, 'json'));
        $form->add('ajouter', SubmitType::class, ['label' => 'Prendre Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getDoctrine()->getManager();
            $event->setMedecin($lab);
            $em->persist($event);
            $em->flush();
            return $this->redirectToRoute('CalendarMed');
        } else {
            return $this->render('tempClient/medecin/Calendrier.html.twig', ['form' => $form->createView(), 'data' => $data, 'dateE' => $dateE, 'dateS' => $dateS,'RDV'=>$dataRDV, 'medecin' => $lab]);
        }
    }
}
