<?php

namespace App\Controller;

use App\Entity\Analyse;
use App\Entity\DossierLab;
use App\Entity\Laboratoire;
use App\Form\AnalyseType;
use App\Form\ChangerAnalyseType;
use App\Form\CreateAnalyseType;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class AnalyseController extends AbstractController
{
    /**
     * @Route("/Laboratoire/analyse", name="analyse")
     */
    public function index(): Response
    {
        return $this->render('analyse/index.html.twig', [
            'controller_name' => 'AnalyseController',
        ]);
    }

    /**
     * @Route("/Laboratoire/createAn/{id}", name="createAn")
     */
    public function  createAn(Request $request,$id){

        $an = new Analyse();

        $form=$this->createForm(AnalyseType::class,$an);
        $form->add('Ajouter', SubmitType::class, ['label' => "Ajouter"]);
        $form->handleRequest($request);
        if($form->isSubmitted() && $form->isValid()){
            $dossier = $this->getDoctrine()->getRepository(DossierLab::class)->find($id);
            $em = $this->getDoctrine()->getManager();
            $an->setDossierLab($dossier);
            $em->persist($an);
            $em->flush();
            return $this->redirectToRoute('showAn', ['id'=>$id]);
        }else{
            return  $this->render('tempClient/Laboratoire/CreerAnalyse.html.twig',['form'=> $form->createView()]);
        }
    }

    /**
     * @Route("/Laboratoire/editAn/{id}", name="editAn")
     */
    public function editAn(Request $request,$id)
    {
        $an = $this->getDoctrine()->getRepository(Analyse::class)->find($id);
        $form = $this->createForm(ChangerAnalyseType::class,$an);
        $form->add('changer',SubmitType::class,['label'=>'Changer la description']);
        $form->handleRequest($request);
        if($form->isSubmitted() && ($form->isValid())){
            $em =$this->getDoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('showAn', ['id'=>$id]);
        }else{
            return $this->render('tempClient/Laboratoire/MettreAJourAnalyse.html.twig',['form'=> $form->createView()]);
        }
    }

    /**
     *@Route("/Laboratoire/Dossier/showAn/{id}", name="showAn")
     */
    public function showAn($id)
    {
        $lab = $this->getDoctrine()->getRepository(Laboratoire::class)->find($this->getUser()->getId());
        $Dossier = $this->getDoctrine()->getRepository(DossierLab::class)->find($id);
        $liste = $this->getDoctrine()->getRepository(Analyse::class)->findBy( ['dossierLab' => $Dossier ]);
        return $this->render('tempClient/Laboratoire/AfficherAnalyse.html.twig',  [
            'controller_name' => 'AnalyseController','liste'=>$liste,'id'=>$id
        ]);
    }

    /**
     * @Route("/Laboratoire/deleteAn/{id}", name="deleteAn")
     */
    public function deleteAn($id)
    {
        $an = $this->getDoctrine()->getRepository(Analyse::class)->find($id);
        $em =$this->getDoctrine()->getManager();
        $em->remove($an);
        $em->flush();
        return $this->redirectToRoute('showAn', ['id'=>$id]);
    }
}
