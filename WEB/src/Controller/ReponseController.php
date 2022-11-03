<?php

namespace App\Controller;

use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Question;
use App\Entity\Reponse;
use App\Form\ReponseType;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ReponseController extends AbstractController
{
    /**
     * @Route("/reponse", name="reponse")
     */
    public function index(): Response
    {
        return $this->render('reponse/index.html.twig', [
            'controller_name' => 'ReponseController',
        ]);
    }
    /**
     * @Route("/Medecin/RepondreQuestion/{id}", name="RepondreQuestion")
     */
    public function RepondreQuestion(Request $request,Question $id)
    {

        $med = $this->getDoctrine()
            ->getRepository(Medecin::class)
            ->find($this->getUser());

        $reponse = new Reponse();

        $form = $this->createForm(ReponseType::class, $reponse);
        $form->add('Repondre', SubmitType::class, ['label' => "Repondre"]);
        $form->handleRequest($request);
        if ($form->isSubmitted() && $form->isValid()) {
            $em = $this->getDoctrine()->getManager();
            $reponse->setQuestion($id);
            $reponse->setMedecin($med);
            $em->persist($reponse);
            $em->flush();
            return $this->redirectToRoute('ListerQuestionMed');

        } else {

            return $this->render('/tempClient/reponse/repondre.html.twig',['form'=>$form->createView()]);
        }
    }

    /**
     * @Route("/Medecin/afficherReponses/{id}", name="afficherReponses")
     */
    public function  AfficherMesReponses(Request $request,PaginatorInterface $paginator,Question $id) {

        $user = $this->getUser();

        $question= $this->getDoctrine()
            ->getRepository(Question::class)
            ->find($id);
        $liste=$paginator->paginate(
        // Doctrine Query, not results
            $question->getReponses(),
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            4
        );
        return $this->render('/tempClient/reponse/afficherreponses.html.twig', [
            'controller_name' => 'ReponsesController', 'question' => $question,'reponses'=>$liste
        ]);

    }



    /**
     * @Route("/Patient/ListerReponses/{id}", name="ListerReponses")
     */
    public function ListerReponses(Request $request,PaginatorInterface $paginator,int $id){

        $listQuestion=$this->getDoctrine()
            ->getRepository(Patient::class)
            ->find($this->getUser())->getQuestions();


        $patient = $this->getDoctrine()
            ->getRepository(Patient::class)
            ->find($this->getUser());
        $b= $patient-> getBlocked();

        foreach ($listQuestion as $q ) {
            if ($q->getId()==$id) {
                $quest=$q ;
            }
        }
        $liste=$paginator->paginate(
        // Doctrine Query, not results
            $listQuestion,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            1
        );
        return $this->render('/tempClient/question/mesquestions.html.twig', [
            'controller_name' => 'ReponseController', 'question' => $quest,'listt'=>$liste,'patient'=>$b
        ]);
    }
    /**
     * @Route("/Medecin/supprimerReponse/{id}", name="supprimerReponse")
     */
    public function SupprimerReponse(Reponse $id)
    {
        $this->getUser();

        $listReponse = $this->getDoctrine()
            ->getRepository(Reponse::class)
            ->find($id);


        $em = $this->getDoctrine()->getManager();
        $em->remove($listReponse);
        $em->flush();
        return $this->redirectToRoute('afficherReponses',['id'=>$listReponse->getQuestion()->getId()]);
    }


    /**
     * @Route("/Medecin/modifierReponse/{id}", name="modifierReponse")
     */
    public function modifierReponse(Request $request,$id)
    {
        $this->getUser();


        $listReponse=$this->getDoctrine()
            ->getRepository(Reponse::class)
            ->find($id);

        $form=$this->createForm(ReponseType::class,$listReponse);
        $form->add('modifier',SubmitType::class,['label'=>'Modifier reponse']);
        $form->handleRequest($request);
        if ($form->isSubmitted()&&($form->isValid()))//test
        {
            $em=$this->getdoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('afficherReponses',['id'=>$listReponse->getQuestion()->getId()]);

        }
        else {
            return $this->render('tempClient/Reponse/modifier.html.twig',
                ['form'=>$form->createView(),'listt','listQuestion'=> $listReponse
                ]);


        }

    }
}
