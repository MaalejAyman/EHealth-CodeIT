<?php

namespace App\Controller;

use App\Entity\Maladie;
use App\Form\MaladieType;
use App\Repository\MaladieRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;


class MaladieController extends AbstractController
{
    /**
     * @Route("/Admin/maladie", name="maladie_index", methods={"GET"})
     */
    public function index(MaladieRepository $maladieRepository,PaginatorInterface $paginator,Request $request): Response
    {
        $donnes =$this->getDoctrine()->getRepository(Maladie::class)->findAll();
        $maladies=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            12
        );
        return $this->render('/tempAdmin/maladie/index.html.twig', [
            'maladies' => $maladies,
        ]);
    }

    /**
     * @Route("/Admin/maladie/new", name="maladie_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $maladie = new Maladie();
        $form = $this->createForm(MaladieType::class, $maladie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($maladie);
            $entityManager->flush();

            return $this->redirectToRoute('maladie_index');
        }

        return $this->render('tempAdmin/maladie/new.html.twig', [
            'maladie' => $maladie,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/maladie/{id}", name="maladie_show", methods={"GET"})
     */
    public function show(Maladie $maladie): Response
    {
        return $this->render('tempAdmin/maladie/show.html.twig', [
            'maladie' => $maladie,
        ]);
    }

    /**
     * @Route("/Admin/maladie/{id}/edit", name="maladie_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Maladie $maladie): Response
    {
        $form = $this->createForm(MaladieType::class, $maladie);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('maladie_index');
        }

        return $this->render('tempAdmin/maladie/edit.html.twig', [
            'maladie' => $maladie,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/maladie/{id}", name="maladie_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Maladie $maladie): Response
    {
        if ($this->isCsrfTokenValid('delete'.$maladie->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($maladie);
            $entityManager->flush();
        }

        return $this->redirectToRoute('maladie_index');
    }
}
