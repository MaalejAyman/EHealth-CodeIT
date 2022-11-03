<?php

namespace App\Controller;

use App\Entity\Analyse;
use App\Entity\DossierLab;
use App\Entity\Laboratoire;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use App\Form\DossierLabType;
use App\Repository\DossierLabRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


class DossierLabController extends AbstractController
{
    /**
     * @Route("/Dossier_Lab",name="laboratoire")
     */
    public function index(DossierLabRepository $dossierLabRepository): Response
    {
        return $this->render('dossier_lab/index.html.twig', [
            'dossier_labs' => $dossierLabRepository->findAll(),
        ]);
    }

    /**
     * @Route("/Dossier_Lab/new", name="dossier_lab_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $dossierLab = new DossierLab();
        $form = $this->createForm(DossierLabType::class, $dossierLab);
        $lab=$this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $pts=$this->getDoctrine()->getRepository(Laboratoire::class)->GetPatientByRdvLab($lab);

        $ptsF =[];
        foreach($pts as $p){
            $dos=$this->getDoctrine()->getRepository(DossierLab::class)->findBy([
                'Laboratoire'=>$lab,
                'patient'=>$p
            ]);
            if($dos==null) {
                $ptsF[$p->getNom() . ' ' . $p->getPrenom()] = $p;
            }
        }
        $form->add('patient',ChoiceType::class,[
            'choices' => $ptsF
        ]);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $laboratoire = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
            $dossierLab->setLaboratoire($laboratoire);
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($dossierLab);
            $entityManager->flush();
            return $this->redirectToRoute('dossier_lab_show');
        }

        return $this->render('/tempClient/dossier_lab/new.html.twig', [
            'dossier_lab' => $dossierLab,
            'form' => $form->createView(),
        ]);
    }
    /**
     * @Route("/Dossier_Lab/new/{id_rdv}", name="dossier_lab_newById", methods={"GET","POST"})
     */
    public function new2(Request $request,Rendezvous $id_rdv): Response
    {
        $dossierLab = new DossierLab();
            $dossierLab->setLaboratoire($id_rdv->getLaboratoire());
            $dossierLab->setPatient($id_rdv->getPatient());
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($dossierLab);
            $entityManager->flush();
            return $this->redirectToRoute('MiseAJourRendezvousLab',['id'=>$id_rdv->getId()]);
    }

    /**
     * @Route("/Dossier_Lab/{id}", name="dossier_lab_show", methods={"GET"})
     */
    public function show(DossierLab $dossierLab): Response
    {
        return $this->render('/tempClient/dossier_lab/show.html.twig', [
            'dossier_lab' => $dossierLab,
        ]);
    }

    /**
     * @Route("/Dossier_Lab/{id}/edit", name="dossier_lab_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, DossierLab $dossierLab): Response
    {
        $form = $this->createForm(DossierLabType::class, $dossierLab);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('dossier_lab_index');
        }

        return $this->render('dossier_lab/edit.html.twig', [
            'dossier_lab' => $dossierLab,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Dossier_Lab/Remove/{id}", name="dossier_lab_delete")
     */
    public function delete(DossierLab $id)
    {
        $an = $this->getDoctrine()->getRepository(DossierLab::class)->find($id);
        $em =$this->getDoctrine()->getManager();
        $em->remove($an);
        $em->flush();
        return $this->redirectToRoute('dossier_lab_show');


        /*      if ($this->isCsrfTokenValid('delete' . $dossierLab->getId(), $request->request->get('_token'))) {
                  $entityManager = $this->getDoctrine()->getManager();
                  $entityManager->remove($dossierLab);
                  $entityManager->flush();
              }

             return $this->redirectToRoute('dossier_lab_show');
      */
    }


  }
