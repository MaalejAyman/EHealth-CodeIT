<?php

namespace App\Repository;

use App\Entity\DossierLab;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method DossierLab|null find($id, $lockMode = null, $lockVersion = null)
 * @method DossierLab|null findOneBy(array $criteria, array $orderBy = null)
 * @method DossierLab[]    findAll()
 * @method DossierLab[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class DossierLabRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, DossierLab::class);
    }

    // /**
    //  * @return DossierLab[] Returns an array of DossierLab objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('d')
            ->andWhere('d.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('d.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?DossierLab
    {
        return $this->createQueryBuilder('d')
            ->andWhere('d.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
