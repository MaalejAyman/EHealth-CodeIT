<?php

namespace App\Controller;

use App\Entity\Medicament;
use App\Form\MedicamentType;
use App\Repository\MedicamentRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class MedicamentController extends AbstractController
{
    /**
     * @Route("/Admin/medicament/", name="medicament_index", methods={"GET"})
     */
    public function index(MedicamentRepository $medicamentRepository,Request $request,PaginatorInterface $paginator): Response
    {
        $donnes=$medicamentRepository->findAll();
        $medicaments=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            12
        );
        return $this->render('tempAdmin/medicament/index.html.twig', [
            'medicaments' => $medicaments,
        ]);
    }

    /**
     * @Route("/Admin/medicament/new", name="medicament_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $medicament = new Medicament();
        $form = $this->createForm(MedicamentType::class, $medicament);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($medicament);
            $entityManager->flush();

            return $this->redirectToRoute('medicament_index');
        }

        return $this->render('tempAdmin/medicament/new.html.twig', [
            'medicament' => $medicament,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/medicament/{id}", name="medicament_show", methods={"GET"})
     */
    public function show(Medicament $medicament): Response
    {
        return $this->render('tempAdmin/medicament/show.html.twig', [
            'medicament' => $medicament,
        ]);
    }

    /**
     * @Route("/Admin/medicament/{id}/edit", name="medicament_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Medicament $medicament): Response
    {
        $form = $this->createForm(MedicamentType::class, $medicament);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('medicament_index');
        }

        return $this->render('tempAdmin/medicament/edit.html.twig', [
            'medicament' => $medicament,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/medicament/{id}", name="medicament_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Medicament $medicament): Response
    {
        if ($this->isCsrfTokenValid('delete'.$medicament->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($medicament);
            $entityManager->flush();
        }

        return $this->redirectToRoute('medicament_index');
    }
}
