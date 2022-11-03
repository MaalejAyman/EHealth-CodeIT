<?php

namespace App\Repository;

use App\Entity\Dossier;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Medecin|null find($id, $lockMode = null, $lockVersion = null)
 * @method Medecin|null findOneBy(array $criteria, array $orderBy = null)
 * @method Medecin[]    findAll()
 * @method Medecin[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class MedecinRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Medecin::class);
    }
    /**
     * @return Patient[] Returns an array of Medecin objects
     */

    public function GetPatientByRdvMed(Medecin $m)
    {
        return $this->createQueryBuilder('m')
            ->select('p')
            ->from(Rendezvous::class,'rdv')
            ->from(Patient::class,'p')
            //->from(Dossier::class,'d')
            ->where('rdv.medecin = :val')
            ->andWhere('rdv.patient = p')
            ->setParameter('val',$m )
            //->andWhere('p <> d.patient')
            ->getQuery()
            ->getResult()
            ;
    }
    // /**
    //  * @return Medecin[] Returns an array of Medecin objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('m')
            ->andWhere('m.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('m.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */
    public function OrderByMailQB(){
        return $this->createQueryBuilder('s')// select
        ->orderBy('s.email','ASC')
            ->getQuery()->getResult();
    }
    /*
    public function findOneBySomeField($value): ?Medecin
    {
        return $this->createQueryBuilder('m')
            ->andWhere('m.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
