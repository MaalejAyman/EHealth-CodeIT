<?php

namespace App\Form;

use App\Entity\Laboratoire;
use Symfony\Component\Form\AbstractType;
use Symfony\Component\Form\Extension\Core\Type\CheckboxType;
use Symfony\Component\Form\Extension\Core\Type\ChoiceType;
use Symfony\Component\Form\Extension\Core\Type\PasswordType;
use Symfony\Component\Form\Extension\Core\Type\RepeatedType;
use Symfony\Component\Form\FormBuilderInterface;
use Symfony\Component\OptionsResolver\OptionsResolver;
use Symfony\Component\Validator\Constraints\IsTrue;
use Symfony\Component\Validator\Constraints\Length;
use Symfony\Component\Validator\Constraints\NotBlank;

class LaboratoireType extends AbstractType
{
    public function buildForm(FormBuilderInterface $builder, array $options)
    {
        $builder
            ->add('email')
          //  ->add('roles')
          ->add('plainPassword', RepeatedType::class, [
              'type' => PasswordType::class,
              'invalid_message' => 'The password fields must match.',
              'options' => ['attr' => ['class' => 'password-field form-control']],
              'required' => true,
              'first_options' => ['label' => 'Mot de passe '],
              'second_options' => ['label' => 'Confirmer  mot de passe'],
              // instead of being set onto the object directly,
              // this is read and encoded in the controller
              'mapped' => false,
              'constraints' => [
                  new NotBlank([
                      'message' => 'Le mot de passe ne peut pas être vide',
                  ]),
                  new Length([
                      'min' => 6,
                      'minMessage' => 'Votre mot de passe doit être au moins {{ limit }} charactères',
                      // max length allowed by Symfony for security reasons
                      'max' => 4096,
                  ]),
              ],])
            ->add('Adresse')
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
            ->add('Tel')
           // ->add('isVerified')
            ->add('num_service')
            ->add('nom')
            ->add('horaire_debut')
            ->add('horaire_fin')
            ->add('services')
            ->add('agreeTerms', CheckboxType::class, [
                'mapped' => false,
                'constraints' => [
                    new IsTrue([
                        'message' => 'You should agree to our terms.',
                    ]),
                ],
            ]);

        ;
    }

    public function configureOptions(OptionsResolver $resolver)
    {
        $resolver->setDefaults([
            'data_class' => Laboratoire::class,
        ]);
    }
}
