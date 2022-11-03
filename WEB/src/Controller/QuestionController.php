<?php

namespace App\Controller;

use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Question;
use App\Form\QuestionType;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class QuestionController extends AbstractController
{
    /**
     * @Route("/question", name="question")
     */
    public function index(): Response
    {
        return $this->render('question/index.html.twig', [
            'controller_name' => 'QuestionController',
        ]);
    }
    /**
     * @Route("/Patient/poserQuestion", name="poserQuestion")
     */
    public function PoserUneQuestion(Request $request)
    {
        $question = new Question();
        $form = $this->createForm(QuestionType::class, $question);
        $form->add('Ajouter', SubmitType::class, ['label' => "Ajouter"]);
        $form->handleRequest($request);
        if ($form->isSubmitted()&&($form->isValid())) {
            $em = $this->getDoctrine()->getManager();
            $question->setPatient($this->getUser());
            $em->persist($question);
            $em->flush();
            return $this->redirectToRoute('ListerQuestion');

        } else {
            return $this->render('/tempClient/question/demander.html.twig', [
                'form' => $form->createView()
            ]);
        }

    }

    /**
     * @Route("/Patient/supprimerQuestion{id}", name="supprimerQuestion")
     */
    public function SupprimerQuestion(Question $id)
    {
        $listQuestion = $this->getDoctrine()
            ->getRepository(Question::class)
            ->find($id);

        $em = $this->getDoctrine()->getManager();
        $em->remove($listQuestion);
        $em->flush();
        return $this->redirectToRoute('ListerQuestion');
    }

    /**
     * @Route("/Patient/modifierQuestion/{id}", name="modifierQuestion")
     */
    public function modifierQuestion(Request $request,$id)
    {
        $listQuestion=$this->getDoctrine()
            ->getRepository(Patient::class)
            ->find($this->getUser())->getQuestions();

        foreach ($listQuestion as $q ) {
            if ($q->getId()==$id) {
                $quest=$q ;
            }
        }

        $form=$this->createForm(QuestionType::class,$quest);
        $form->add('modifier',SubmitType::class,['label'=>'Modifier']);
        $form->handleRequest($request);
        if ($form->isSubmitted()&&($form->isValid()))//test
        {
            $em=$this->getdoctrine()->getManager();
            $em->flush();
            return $this->redirectToRoute('ListerQuestion');

        }
        else {
            return $this->render('/tempClient/question/modifier.html.twig',
                ['form'=>$form->createView(),'listt','listQuestion'=> $listQuestion
                ]);


        }

    }

    /**
     * @Route("/Patient/ListerQuestion", name="ListerQuestion")
     */

    public function ListerQuestion(Request $request,PaginatorInterface $paginator){

        $list = $this->getDoctrine()
            ->getRepository(Patient::class)
            ->find($this->getUser());


        $patient = $this->getDoctrine()
            ->getRepository(Patient::class)
            ->find($this->getUser());
        $b= $patient-> getBlocked();
        $l=$list->getQuestions();
        $liste=$paginator->paginate(
        // Doctrine Query, not results
            $l,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            4
        );
        return $this->render('/tempClient/question/mesquestions.html.twig', [
            'listt' => $liste,'question'=>null,'patient'=>$b
        ]);
    }

    /**
     * @Route("/Medecin/ListerQuestion/Med", name="ListerQuestionMed")
     */
    public function afficherQuestions(Request $request,PaginatorInterface $paginator)
    {
        $user = $this->getUser();
        $medecin = $this->getDoctrine()
            ->getRepository(Medecin::class)
            ->find($user->getId());
        $list = $this->getDoctrine()
            ->getRepository(Question::class)
            ->findBy([
                'specialite'=>$medecin->getSpecialite()
            ]);
        $liste=$paginator->paginate(
        // Doctrine Query, not results
            $list,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            4
        );
        return $this->render('/tempClient/question/touslesquestions.html.twig',['list'=>$liste]);
    }
}
