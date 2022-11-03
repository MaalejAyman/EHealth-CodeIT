<?php

namespace App\Controller;

use App\Entity\Dossier;
use App\Entity\FicheMedicale;
use App\Form\FicheMedicaleType;
use App\Repository\FicheMedicaleRepository;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

/**
 * @Route("Medecin/fiche/medicale")
 */
class FicheMedicaleController extends AbstractController
{
    /**
     * @Route("/{id}", name="fiche_medicale_index", methods={"GET"})
     */
    public function index(FicheMedicaleRepository $ficheMedicaleRepository,Dossier $id): Response
    {
        return $this->render('tempClient/fiche_medicale/index.html.twig', [
            'fiche_medicales' => $ficheMedicaleRepository->findBy([
                'dossier'=>$id
            ]),
        ]);
    }

    /**
     * @Route("/new/{id}", name="fiche_medicale_new")
     */
    public function new(Request $request,Dossier $id): Response
    {

        $this->getUser();
        $ficheMedicale = new FicheMedicale();
        $form = $this->createForm(FicheMedicaleType::class, $ficheMedicale);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $ficheMedicale->setDossier($id);
            $entityManager->persist($ficheMedicale);
            $entityManager->flush();

            return $this->redirectToRoute('dossier_show',['id'=>$id]);
        }

        return $this->render('tempClient/fiche_medicale/new.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/{id}/details", name="fiche_medicale_show", methods={"GET"})
     */
    public function show(FicheMedicale $ficheMedicale): Response
    {
        return $this->render('tempClient/fiche_medicale/show.html.twig', [
            'fiche_medicale' => $ficheMedicale,
        ]);
    }

    /**
     * @Route("/{id}/edit", name="fiche_medicale_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, FicheMedicale $id): Response
    {
        $form = $this->createForm(FicheMedicaleType::class, $id);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('dossier_show',['id'=>$id->getDossier()->getId()]);
        }

        return $this->render('tempClient/fiche_medicale/edit.html.twig', [
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/delete/{id}", name="fiche_medicale_delete")
     */
    public function delete(Request $request, FicheMedicale $ficheMedicale): Response
    {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($ficheMedicale);
            $entityManager->flush();
        return $this->redirectToRoute('dossier_show',['id'=>$ficheMedicale->getDossier()->getId()]);
    }
}
