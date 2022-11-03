<?php

namespace App\Controller;

use App\Entity\Dossier;
use App\Entity\DossierLab;
use App\Entity\Event;
use App\Entity\Laboratoire;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use App\Form\CreaterendezvousType;
use App\Form\MiseAJourRendezVousType;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Security\Http\Authentication\AuthenticationUtils;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\DateTimeNormalizer;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;

class RendezvousController extends AbstractController
{
    /**
     * @Route("Patient/rendezvous", name="rendezvous")
     */
    public function index(): Response
    {
        return $this->render('rendezvous/index.html.twig', [
            'controller_name' => 'RendezvousController',
        ]);
    }

    /**
     * @Route("Patient/ConsulterRendezVous", name="ConsulterRendezVous")
     */
    public function ConsulterRendezVous(Request $request,PaginatorInterface $paginator)
    {
        $this->getUser();
        $p = $this->getDoctrine()->getRepository(Patient::class)->find($this->getUser()->getId());
        $rendezvous = $this->getDoctrine()
            ->getRepository(Rendezvous::class)
            ->findBy([
                'patient' => $p
            ]);
        //$rdv = new Rendezvous();
        $liste=$paginator->paginate(
        // Doctrine Query, not results
            $rendezvous,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            6
        );
            return $this->render('tempClient/rendezvous/ConsultRendezVous.html.twig', [
                'controller_name' => 'RendezVousController', 'rendezvous' => $liste
            ]);
    }

    /**
     * @Route("Patient/prendreRendezVous/{id}", name="PrendreRendezVous")
     */
    public function create(Request $request, Medecin $medecin)
    {
        $rendezvous = new Rendezvous();
        $form = $this->createForm(CreaterendezvousType::class, $rendezvous);
        $event = $this->getDoctrine()->getRepository(Event::class)->findBy(['medecin' => $medecin]);
        $encoders = [new XmlEncoder(), new JsonEncoder()];
        $normalizers = [new ObjectNormalizer(), new DateTimeNormalizer()];
        $norm = [new DateTimeNormalizer()];
        $normalizers[0]->setCircularReferenceHandler(function ($object) {
            return $object->getMedecin();
        });
        $serializer = new Serializer($normalizers, $encoders);
        $serializer1 = new Serializer($norm, $encoders);
        $dateend = [];
        $datestart = [];
        foreach ($event as $ev) {
            $date = $serializer1->serialize($ev->getStart(), 'json');
            $datestart[$ev->getId()] = $date;
        }
        foreach ($event as $ev) {
            $datestart[$ev->getId()] = $ev->getStart();
        }
        foreach ($event as $ev) {
            $dateend[$ev->getId()] = $ev->getEnd();
        }
        $jsonContent = $serializer->serialize([$event], 'json', ['ignored_attributes' => [
            'medecin', 'start', 'end', 'rendezvous'
        ]]);
        $data = json_encode($jsonContent);
        $dateE = json_encode($serializer1->serialize($dateend, 'json'));
        $dateS = json_encode($serializer1->serialize($datestart, 'json'));
        $form->add('ajouter', SubmitType::class, ['label' => 'Prendre Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getDoctrine()->getManager();
            $p = $this->getDoctrine()
                ->getRepository(Patient::class)
                ->find($this->getUser()->getId());


            $rendezvous->setMedecin($medecin);
            $rendezvous->setPatient($p);
            $rendezvous->setEtat('confirmé');
            $event = new Event();
            $datea = $rendezvous->getDate()->format("Y-m-d");
            $hour = $rendezvous->getHeure()->format("H:i:s");

            $y = $datea[0] . $datea[1] . $datea[2] . $datea[3];
            $m = $datea[5] . $datea[6];
            $d = $datea[8] . $datea[9];
            $he = $hour[0] . $hour[1];
            $min = $hour[3] . $hour[4];
            $sec = $hour[6] . $hour[7];
            $em->persist($rendezvous);
            $em->flush();
            $event->setMedecin($medecin);
            $rdv = $this->getDoctrine()
                ->getRepository(Rendezvous::class)
                ->findAll();
            $id = 0;
            foreach ($rdv as $r) {
                $id = $r->getId();
            }
            $event->setRendezvous($id++);
            $event->setDayoff(false);
            $he1 = null;
            $heu = null;
            $hh = $hour[0];
            $hhh = $hour[1];
            if ($he == "23") {
                $heu = "00";
                $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                $event->setEnd($dd);
            } else {
                if ($hhh == '9') {
                    $he1 = (int)$hh + 1;
                    $heu = strval($he1) . '0';
                    $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                    $event->setEnd($dd);
                } else {
                    $he1 = (int)$hhh + 1;
                    $heu = $hh . strval($he1);
                    $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                    $event->setEnd($dd);
                }
            }
            $d = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $he . ':' . $min . ':' . $sec);
            $event->setStart($d);
            $event->setTitle("rendezvous");
            $event->setDescription("test");
            $em->persist($event);

            $em->flush();
            return $this->redirectToRoute('ConsulterRendezVous');
        } else {
            return $this->render('tempClient/rendezvous/Calendrier.html.twig', ['form' => $form->createView(), 'data' => $data, 'dateE' => $dateE, 'dateS' => $dateS, 'medecin' => $medecin]);
        }

    }

    /**
     * @Route("/Patient/prendreRendezVousLabo/{id}", name="prendreRendezVousLabo")
     */
    public function create2(Request $request, Laboratoire $lab)
    {

        $rendezvous = new Rendezvous();
        $form = $this->createForm(CreaterendezvousType::class, $rendezvous);
        $event = $this->getDoctrine()->getRepository(Event::class)->findBy(['laboratoire' => $lab]);
        $encoders = [new XmlEncoder(), new JsonEncoder()];
        $normalizers = [new ObjectNormalizer(), new DateTimeNormalizer()];
        $norm = [new DateTimeNormalizer()];
        $normalizers[0]->setCircularReferenceHandler(function ($object) {
            return $object->getLaboratoire();
        });
        $serializer = new Serializer($normalizers, $encoders);
        $serializer1 = new Serializer($norm, $encoders);
        $dateend = [];
        $datestart = [];
        foreach ($event as $ev) {
            $date = $serializer1->serialize($ev->getStart(), 'json');
            $datestart[$ev->getId()] = $date;
        }
        foreach ($event as $ev) {
            $datestart[$ev->getId()] = $ev->getStart();
        }
        foreach ($event as $ev) {
            $dateend[$ev->getId()] = $ev->getEnd();
        }
        $jsonContent = $serializer->serialize([$event], 'json', ['ignored_attributes' => [
            'laboratoire', 'start', 'end', 'rendezvous'
        ]]);
        $data = json_encode($jsonContent);
        $dateE = json_encode($serializer1->serialize($dateend, 'json'));
        $dateS = json_encode($serializer1->serialize($datestart, 'json'));
        $form->add('ajouter', SubmitType::class, ['label' => 'Prendre Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getDoctrine()->getManager();
            $p = $this->getDoctrine()
                ->getRepository(Patient::class)
                ->find($this->getUser()->getId());


            $rendezvous->setLaboratoire($lab);
            $rendezvous->setPatient($p);
            $rendezvous->setEtat('confirmé');
            $event = new Event();
            $datea = $rendezvous->getDate()->format("Y-m-d");
            $hour = $rendezvous->getHeure()->format("H:i:s");

            $y = $datea[0] . $datea[1] . $datea[2] . $datea[3];
            $m = $datea[5] . $datea[6];
            $d = $datea[8] . $datea[9];
            $he = $hour[0] . $hour[1];
            $min = $hour[3] . $hour[4];
            $sec = $hour[6] . $hour[7];
            $em->persist($rendezvous);
            $em->flush();
            $event->setLaboratoire($lab);
            $rdv = $this->getDoctrine()
                ->getRepository(Rendezvous::class)
                ->findAll();
            $id = 0;
            foreach ($rdv as $r) {
                $id = $r->getId();
            }
            $event->setRendezvous($id++);
            $event->setDayoff(false);
            $he1 = null;
            $heu = null;
            $hh = $hour[0];
            $hhh = $hour[1];
            if ($he == "23") {
                $heu = "00";
                $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                $event->setEnd($dd);
            } else {
                if ($hhh == '9') {
                    $he1 = (int)$hh + 1;
                    $heu = strval($he1) . '0';
                    $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                    $event->setEnd($dd);
                } else {
                    $he1 = (int)$hhh + 1;
                    $heu = $hh . strval($he1);
                    $dd = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $heu . ':' . $min . ':' . $sec);
                    $event->setEnd($dd);
                }
            }
            $d = \DateTime::createFromFormat('Y-m-d H:i:s', $y . '-' . $m . '-' . $d . ' ' . $he . ':' . $min . ':' . $sec);
            $event->setStart($d);
            $event->setTitle("rendezvous");
            $event->setDescription("test");
            $em->persist($event);

            $em->flush();
            return $this->redirectToRoute('ConsulterRendezVous');
        } else {
            return $this->render('tempClient/rendezvous/Calendrier.html.twig', ['form' => $form->createView(), 'data' => $data, 'dateE' => $dateE, 'dateS' => $dateS, 'medecin' => $lab]);
        }

    }

    /**
     * @Route("Patient/AnnulerRendezVous/{id}", name="AnnulerRendezVous")
     */
    public function AnnulerRendezVous(Rendezvous $rendezvous)
    {
        $event = $this->getDoctrine()->getRepository(Event::class)->findOneBy([
            'rendezvous' => $rendezvous->getId()
        ]);
        $patient = $rendezvous->getPatient();
        $nbAlerts = $patient->getNbAlert();
        if ($nbAlerts < 5 && $patient->getBlockedRDV() != 1) {
            $em = $this->getDoctrine()->getManager();
            $patient->setNbAlert($nbAlerts + 1);
            $em->remove($rendezvous);
            $em->remove($event);
            $em->flush();
        }
        if ($nbAlerts + 1 == 5 && $patient->getBlockedRDV() != 1) {
            $em = $this->getDoctrine()->getManager();
            $patient->setBlockedRDV(1);
            $em->flush();
        }
        return $this->redirectToRoute('ConsulterRendezVous');
    }

    /**
     * @Route("/Laboratoire/MiseAJourRendezvous/{id}", name="MiseAJourRendezvousLab")
     */
    public function MiseAJourRendezvous(Request $request, $id)
    {
        $user = $this->getUser();
        $list = $this->getDoctrine()
            ->getRepository(Rendezvous::class)
            ->find($id);
        $ds =$this->getDoctrine()->getRepository(DossierLab::class)->findOneBy([
            'patient'=> $list->getPatient(),
            'Laboratoire' => $list->getLaboratoire()
        ]);
        $form = $this->createForm(MiseAJourRendezVousType::class, $list);
        $form->add('update', SubmitType::class, ['label' => 'Mettre a jour Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getdoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('CalendarLab');
        } else {
            return $this->render('tempClient/rendezvous/MiseAJourRendezVous.html.twig',
                ['form' => $form->createView(),'rdv'=>$list,'dossier'=>$ds]);
        }
    }
    /**
     * @Route("/Medecin/MiseAJourRendezvous/{id}", name="MiseAJourRendezvousMed")
     */
    public function MiseAJourRendezvousMed(Request $request, $id)
    {
        $user = $this->getUser();
        $list = $this->getDoctrine()
            ->getRepository(Rendezvous::class)
            ->find($id);
        $ds =$this->getDoctrine()->getRepository(Dossier::class)->findOneBy([
            'patient'=> $list->getPatient(),
            'medecin' => $user
        ]);
        $form = $this->createForm(MiseAJourRendezVousType::class, $list);
        $form->add('update', SubmitType::class, ['label' => 'Mettre a jour Rendez Vous']);
        $form->handleRequest($request);
        if ($form->isSubmitted() && ($form->isValid())) {
            $em = $this->getdoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('CalendarMed');
        } else {
            return $this->render('tempClient/rendezvous/MiseAJourRendezVousMed.html.twig',
                ['form' => $form->createView(),'rdv'=>$list,'dossier'=>$ds]);
        }
    }
    /**
     * @Route("/Patient/evaluerservice/{id}", name="evaluerservice")
     */
    public function evaluerService(Request $request, $id)
    {
        $user = $this->getUser();
        $list = $this->getDoctrine()
            ->getRepository(Rendezvous::class)
            ->find($id);
        return $this->render('tempClient/rendezvous/evaluerservice.html.twig', [
            'controller_name' => 'RendezvousController', 'list' => $list
        ]);

    }

    /**
     * @Route("/Patient/mettreevaluation/{id}/{id_rdv}", name="mettreevaluation")
     */
    public function mettreEvaluation(Request $request, $id, $id_rdv)
    {
        $user = $this->getUser();
        $em = $this->getDoctrine()->getManager();
        $rdv = $em->getRepository(Rendezvous::class)->find($id_rdv);
        $rdv->setEvaluation($id);
        $em->flush();
        if ($rdv->getLaboratoire() != null) {
            $rdvs = $em->getRepository(Rendezvous::class)->findBy([
                'laboratoire'=>$rdv->getLaboratoire(),
                'etat'=> 'effectué'
            ]);
            $sm=0;
            $i=0;
            foreach ($rdvs as $r){
                if($r->getEvaluation()!=null) {
                    $sm = $sm + $r->getEvaluation();
                    $i++;
                }
            }
            $n=intval(round($sm/$i));
            $rdv->getLaboratoire()->setNote($n);
        }
        else {
            $rdvs = $em->getRepository(Rendezvous::class)->findBy([
                'medecin' =>$rdv->getMedecin(),
                'etat' =>'effectué'
            ]);
            $sm=0;
            $i=0;
            foreach ($rdvs as $r){
                if($r->getEvaluation()!=null) {
                    $sm = $sm + $r->getEvaluation();
                    $i++;
                }
            }
            $n=intval(round($sm/$i));
            $rdv->getMedecin()->setNote($n);
        }
        $em->flush();
        return $this->redirectToRoute('ConsulterRendezVous');
    }
}
