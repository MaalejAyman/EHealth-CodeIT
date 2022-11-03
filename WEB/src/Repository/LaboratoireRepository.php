<?php

namespace App\Repository;

use App\Entity\DossierLab;
use App\Entity\Laboratoire;
use App\Entity\Medecin;
use App\Entity\Patient;
use App\Entity\Rendezvous;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;

/**
 * @method Laboratoire|null find($id, $lockMode = null, $lockVersion = null)
 * @method Laboratoire|null findOneBy(array $criteria, array $orderBy = null)
 * @method Laboratoire[]    findAll()
 * @method Laboratoire[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class LaboratoireRepository extends ServiceEntityRepository
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, Laboratoire::class);
    }

    /**
     * @return Laboratoire[] Returns an array of Laboratoire objects
     */

    public function findByBothFieldsLab($ville,$service,$txt)
    {
        $query= $this->createQueryBuilder('m');
        if($ville!=null && $ville!=""){
            $query->andWhere('m.ville = :val1')
                ->setParameter('val1', $ville);
        };

        if($txt!=null && $txt!=""){
            $query->andWhere('m.nom like :val3')
                ->setParameter('val3', $txt);
        };

        $query->orderBy('m.nom', 'ASC');

        return $query->getQuery()->getResult();
    }

    public function OrderByMailQB(){
        return $this->createQueryBuilder('s')// select
        ->orderBy('s.email','ASC')
            ->getQuery()->getResult();
    }
    /*
    public function findOneBySomeField($value): ?Laboratoire
    {
        return $this->createQueryBuilder('l')
            ->andWhere('l.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
    /**
     * @return Patient[] Returns an array of Patient objects
     */
    public function GetPatientByRdvLab(Laboratoire $m)
    {
        return $this->createQueryBuilder('l')
            ->select('p')
            //->from(DossierLab::class,'d')
            ->from(Rendezvous::class,'rdv')
            ->from(Patient::class,'p')
            ->where('rdv.laboratoire = :val')
            ->andWhere('rdv.patient = p')
            //->andWhere('d.patient != p')
            ->setParameter('val',$m )
            ->getQuery()
            ->getResult();
    }
}
