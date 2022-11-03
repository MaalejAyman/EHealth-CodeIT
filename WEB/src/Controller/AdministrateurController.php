<?php

namespace App\Controller;

use Symfony\Bundle\FrameworkBundle\Controller\AbstractController;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\Routing\Annotation\Route;

class AdministrateurController extends AbstractController
{
    /**
     * @Route("/Admin/acceuil", name="acceuil")
     */
    public function acceuil(): Response
    {
        return $this->render('tempAdmin/admin/index.html.twig');
    }

}
