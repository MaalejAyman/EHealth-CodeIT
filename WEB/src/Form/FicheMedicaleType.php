<?php

namespace App\Form;

use App\Entity\FicheMedicale;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;

class FicheMedicaleType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('remarque')
            ->add('description')
            //->add('dossier')
            ->add('maladie')
            ->add('medicaments')
        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => FicheMedicale::class,
        ]);
    }
}
