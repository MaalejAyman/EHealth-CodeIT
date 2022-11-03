<?php

namespace App\Controller;

use App\Entity\Service;
use App\Form\ServiceType;
use App\Repository\ServiceRepository;
use Knp\Component\Pager\PaginatorInterface;
use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class ServiceController extends AbstractController
{
    /**
     * @Route("/Admin/service", name="service_index", methods={"GET"})
     */
    public function index(ServiceRepository $serviceRepository,PaginatorInterface $paginator,Request $request): Response
    {
        $donnes =$this->getDoctrine()->getRepository(Service::class)->findAll();
        $services=$paginator->paginate(
        // Doctrine Query, not results
            $donnes,
            // Define the page parameter
            $request->query->getInt('page', 1),
            // Items per page
            12
        );
        return $this->render('/tempAdmin/service/index.html.twig', [
            'services' => $services,
        ]);
    }

    /**
     * @Route("/Admin/service/new", name="service_new", methods={"GET","POST"})
     */
    public function new(Request $request): Response
    {
        $service = new Service();
        $form = $this->createForm(ServiceType::class, $service);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->persist($service);
            $entityManager->flush();

            return $this->redirectToRoute('service_index');
        }

        return $this->render('tempAdmin/service/new.html.twig', [
            'service' => $service,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/service/{id}", name="service_show", methods={"GET"})
     */
    public function show(Service $service): Response
    {
        return $this->render('tempAdmin/service/show.html.twig', [
            'service' => $service,
        ]);
    }

    /**
     * @Route("/Admin/service/{id}/edit", name="service_edit", methods={"GET","POST"})
     */
    public function edit(Request $request, Service $service): Response
    {
        $form = $this->createForm(ServiceType::class, $service);
        $form->handleRequest($request);

        if ($form->isSubmitted() && $form->isValid()) {
            $this->getDoctrine()->getManager()->flush();

            return $this->redirectToRoute('service_index');
        }

        return $this->render('tempAdmin/service/edit.html.twig', [
            'service' => $service,
            'form' => $form->createView(),
        ]);
    }

    /**
     * @Route("/Admin/service/{id}", name="service_delete", methods={"DELETE"})
     */
    public function delete(Request $request, Service $service): Response
    {
        if ($this->isCsrfTokenValid('delete'.$service->getId(), $request->request->get('_token'))) {
            $entityManager = $this->getDoctrine()->getManager();
            $entityManager->remove($service);
            $entityManager->flush();
        }

        return $this->redirectToRoute('service_index');
    }
}
