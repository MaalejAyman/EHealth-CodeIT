<?php

namespace App\Controller;

use App\Entity\Dossier;
use App\Entity\DossierLab;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use App\Form\DossierType;
use App\Repository\DossierRepository;
use App\Repository\FicheMedicaleRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;
use Symfony\Component\Serializer\Encoder\JsonEncoder;
use Symfony\Component\Serializer\Encoder\XmlEncoder;
use Symfony\Component\Serializer\Normalizer\ObjectNormalizer;
use Symfony\Component\Serializer\Serializer;

/**
 * @Route("/Medecin/dossier")
 */
class DossierController extends AbstractController
{
    /**
     * @Route("/", name="dossier_index", methods={"GET"})
     */
    public function index(DossierRepository $dossierRepository): Response
    {
        return $this->render('/tempClient/dossier/index.html.twig', [
            'dossiers' => $dossierRepository->findBy([
                'medecin'=> $this->getUser()->getId()
            ]),
        ]);
    }

    /**
     * @Route("/new", name="dossier_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $dossier = new Dossier();
        $med=$this->getDoctrine()->getRepository(Medecin::class)->find($this->getUser()->getId());
        $pts=$this->getDoctrine()->getRepository(Medecin::class)->GetPatientByRdvMed($med);

        $form =$this->createForm(DossierType::class, $dossier);
        $ptsF =[];
        foreach($pts as $p){
            $dos=$this->getDoctrine()->getRepository(Dossier::class)->findBy([
                'medecin'=>$med,
                'patient'=>$p
            ]);
            if($dos==null) {
                $ptsF[$p->getNom() . ' ' . $p->getPrenom()] = $p;
            }
        }
        //dd( $ptsF);
        $form->add('patient',ChoiceType::class,[
                'choices' => $ptsF
        ]);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
$dossier->setMedecin($med);
            $entityManager->persist($dossier);
            $entityManager->flush();
            return $this->redirectToRoute('dossier_index');
        }

        return $this->render('/tempClient/dossier/new.html.twig', [
            'dossier' => $dossier,
            'form' => $form->createView(),
        ]);
    }
    /**
     * @Route("/new/{id_rdv}", name="dossier_newByID", methods={"GET","POST"})
     */
    public function new2(Request $request,Rendezvous $id_rdv): Response
    {
        $dossier = new Dossier();
        $dossier->setMedecin($id_rdv->getMedecin());
        $dossier->setPatient($id_rdv->getPatient());
        $entityManager = $this->getDoctrine()->getManager();
        $entityManager->persist($dossier);
        $entityManager->flush();
        return $this->redirectToRoute('MiseAJourRendezvousMed',['id'=>$id_rdv->getId()]);
    }
    /**
     * @Route("/{id}", name="dossier_show",methods={"GET"})
     */
    public function show(Dossier $dossier): Response
    {
        $this->getUser();
        return $this->render('/tempClient/dossier/show.html.twig', [
            'dossier' => $dossier,
        ]);
    }

    /**
     * @Route("/{id}/edit", name="dossier_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Dossier $dossier): Response
    {
        $form = $this->createForm(DossierType::class, $dossier);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('dossier_index');
        }

        return $this->render('/tempClient/dossier/edit.html.twig', [
            'dossier' => $dossier,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/{id}", name="dossier_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Dossier $dossier): Response
    {
        if ($this->isCsrfTokenValid('delete'.$dossier->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($dossier);
            $entityManager->flush();
        }

        return $this->redirectToRoute('dossier_index');
    }
    /**
     * @Route("/test/test", name="testFunc")
     */
    public function test(Request $request): Response
    {
        $med=$this->getDoctrine()->getRepository(Medecin::class)->find($this->getUser()->getId());
        $pts=$this->getDoctrine()->getRepository(Medecin::class)->GetPatientByRdvMed($med);
        dd($pts);
        return $this->redirectToRoute('dossier_index');
    }
    /**
     * @Route("/statsD/{id}" , name="doc_stats")
     */
    public function statistiques(Dossier $id, FicheMedicaleRepository $ficherepo){
        $fiches = $ficherepo->findBy([
            'dossier'=>$id
        ]);
        $fichecount= [];
        $fichemaladie=[];
        $fichemedicament=[];
        foreach ($fiches as $fiche ){
            //nbr des fiches = nbr des visites
            //liste  des maladies
            $fichemaladie[]= $fiche->getMaladie();
            //liste des medicaments pris
            $fichemedicament[]= $fiche->getMedicaments();


        }
        dd($fichemaladie);

        $encoders = [new XmlEncoder(), new JsonEncoder()];
        $normalizers = [new ObjectNormalizer()];
        $normalizers[0]->setCircularReferenceHandler(function ($object) {
            return $object->getMedicaments();
        });
        $serializer = new Serializer($normalizers, $encoders);

        $jsonContent = $serializer->serialize([$fichemedicament], 'json', ['ignored_attributes' => [
            'fiche',
        ]]);
        return $this->render('/tempClient/dossier/statistiques.html.twig',
            ['id' => $id,
                'fichemaladie'=> json_encode($fichemaladie) ,
                'fichemedicament'=> json_encode($fichemedicament) ,
                'medicamentcount'=> json_encode($medicamentcount) ,

            ]
        );
    }
}
