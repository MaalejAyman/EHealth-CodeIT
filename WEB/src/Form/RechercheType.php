<?php

namespace App\Form;

use App\Entity\Service;
use App\Entity\Specialite;
use Symfony\Bridge\Doctrine\Form\Type\EntityType;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\SubmitType;
use Symfony\Component\Form\Extension\Core\Type\TextType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\Form\FormEvent;
use Symfony\Component\Form\FormEvents;
use Symfony\Component\OptionsResolver\OptionsResolver;

class RechercheType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('rech', TextType::class)
            ->add('Ville', ChoiceType::class, [
                'choices' => [
                    'Tunis' => 'Tunis',
                    'Ariana' => 'Ariana',
                    'Ben Arous' => 'Ben Arous',
                    'Manouba' => 'Manouba',
                    'Nabeul' => 'Nabeul',
                    'Zaghouan' => 'Zaghouan',
                    'Bizerte' => 'Bizerte',
                    'Béja' => 'Béja',
                    'Jendouba' => 'Jendouba',
                    'Kef' => 'Kef',
                    'Siliana' => 'Siliana',
                    'Sousse' => 'Sousse',
                    'Monastir' => 'Monastir',
                    'Mahdia' => 'Mahdia',
                    'Sfax' => 'Sfax',
                    'Kairouan' => 'Kairouan',
                    'Kasserine' => 'Kasserine',
                    'Sidi Bouzid' => 'Sidi Bouzid',
                    'Gabès' => 'Gabès',
                    'Mednine' => 'Mednine',
                    'Tataouine' => 'Tataouine',
                    'Gafsa' => 'Gafsa',
                    'Tozeur' => 'Tozeur',
                    'Kebili' => 'Kebili',],
                'placeholder'=>'Selectionner la ville'
            ])
            ->add('Specialite',EntityType::class,[
                'class' => Specialite::class,
                'choice_label' => 'nomSpec',
                'placeholder'=>'Selectionner la Specialite du medecin'
            ])
            ->add('Service',EntityType::class,[
                'class' => Service::class,
                'choice_label' => 'nomService',
                'placeholder'=>'Selectionner le service donner par le laboratoire'
            ]);
            //->add('Rechercher',SubmitType::class);
            /*->addEventListener(
                FormEvents::PRE_SET_DATA,
                function(FormEvent $event){
                    $form= $event->getForm();
                    $data= $event->getData();
                    $Type=$form->get('Type')->getData();
                    if(!$Type){
                        $form->get('Type')->setData('Medecin');
                    }
                    $Type=$form->get('Type')->getData();

                    if($Type && $Type =='Medecin'){
                        $form->add('Specialite',EntityType::class,[
                            'class' => Specialite::class,
                            'choice_label' => 'nomSpec',
                        ]);
                        $form->remove('Service');
                    }else{
                        $form->add('Service',EntityType::class,[
                            'class' => Service::class,
                            'choice_label' => 'nomService',
                        ]);
                        $form->remove('Specialite');
                    }
                }
            );*/

    }

    public function configureOptions(OptionsResolver $resolver)
    {

    }
}
