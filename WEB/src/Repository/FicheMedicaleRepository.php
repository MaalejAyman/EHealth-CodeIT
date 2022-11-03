<?php

namespace App\Repository;

use App\Entity\FicheMedicale;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method FicheMedicale|null find($id, $lockMode = null, $lockVersion = null)
 * @method FicheMedicale|null findOneBy(array $criteria, array $orderBy = null)
 * @method FicheMedicale[]    findAll()
 * @method FicheMedicale[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class FicheMedicaleRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, FicheMedicale::class);
    }

    // /**
    //  * @return FicheMedicale[] Returns an array of FicheMedicale objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('f')
            ->andWhere('f.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('f.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?FicheMedicale
    {
        return $this->createQueryBuilder('f')
            ->andWhere('f.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
