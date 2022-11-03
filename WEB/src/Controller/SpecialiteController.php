<?php

namespace App\Controller;

use App\Entity\Specialite;
use App\Form\SpecialiteType;
use App\Repository\MedicamentRepository;
use App\Repository\SpecialiteRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class  SpecialiteController extends AbstractController
{
    /**
     * @Route("/Admin/Specialite", name="specialite_index", methods={"GET"})
     */
        public function index(SpecialiteRepository $specialiteRepository,Request $request,PaginatorInterface $paginator): Response
    {
        $donnes=$specialiteRepository->findAll();
        $Specialites=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            12
        );
        return $this->render('tempAdmin/specialite/index.html.twig', [
            'specialites' => $Specialites,
        ]);
    }

    /**
     * @Route("/Admin/Specialite/new", name="specialite_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $specialite = new Specialite();
        $form = $this->createForm(SpecialiteType::class, $specialite);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($specialite);
            $entityManager->flush();

            return $this->redirectToRoute('specialite_index');
        }

        return $this->render('tempAdmin/specialite/new.html.twig', [
            'specialite' => $specialite,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/Specialite/{id}", name="specialite_show", methods={"GET"})
     */
    public function show(Specialite $specialite): Response
    {
        return $this->render('tempAdmin/specialite/show.html.twig', [
            'specialite' => $specialite,
        ]);
    }

    /**
     * @Route("/Admin/Specialite/{id}/edit", name="specialite_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Specialite $specialite): Response
    {
        $form = $this->createForm(SpecialiteType::class, $specialite);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('specialite_index');
        }

        return $this->render('tempAdmin/specialite/edit.html.twig', [
            'specialite' => $specialite,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/Specialite/{id}", name="specialite_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Specialite $specialite): Response
    {
        if ($this->isCsrfTokenValid('delete'.$specialite->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($specialite);
            $entityManager->flush();
        }

        return $this->redirectToRoute('specialite_index');
    }
}
