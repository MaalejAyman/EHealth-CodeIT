<?php

namespace App\Repository;

use App\Entity\Laboratoire;
use App\Entity\Medecin;
use App\Entity\User;
use Doctrine\Bundle\DoctrineBundle\Repository\ServiceEntityRepository;
use Doctrine\Persistence\ManagerRegistry;
use Symfony\Component\Security\Core\Exception\UnsupportedUserException;
use Symfony\Component\Security\Core\User\PasswordUpgraderInterface;
use Symfony\Component\Security\Core\User\UserInterface;

/**
 * @method User|null find($id, $lockMode = null, $lockVersion = null)
 * @method User|null findOneBy(array $criteria, array $orderBy = null)
 * @method User[]    findAll()
 * @method User[]    findBy(array $criteria, array $orderBy = null, $limit = null, $offset = null)
 */
class UserRepository extends ServiceEntityRepository implements PasswordUpgraderInterface
{
    public function __construct(ManagerRegistry $registry)
    {
        parent::__construct($registry, User::class);
    }

    /**
     * Used to upgrade (rehash) the user's password automatically over time.
     */
    public function upgradePassword(UserInterface $user, string $newEncodedPassword): void
    {
        if (!$user instanceof User) {

            throw new UnsupportedUserException(sprintf('Instances of "%s" are not supported.', \get_class($user)));
        }
        $user->setPassword($newEncodedPassword);
        $this->_em->persist($user);
        $this->_em->flush();
    }
    /**
     * @return Laboratoire[] Returns an array of Laboratoire objects
     */

    public function findByBothFieldsLab($ville,$service,$txt)
    {
        $query= $this->createQueryBuilder('u')
            ->from(Laboratoire::class,'l')
            ->select('l.nom,u.id,u.Ville,u.email,u.Tel,l.note')
            ->where('u.id = l.id');
        if($ville!=null && $ville!=""){
            $query->andWhere('u.Ville = :val1')
                ->setParameter('val1', $ville);
        };
        if($txt!=null && $txt!=""){
            $query->andWhere('l.nom like :val2')
                ->setParameter('val2', '%'.$txt.'%');
        };
        if($service!=null && $service!=""){
            $query->andWhere(':val3 member of l.services')
                ->setParameter('val3', $service);
        };

        return $query->getQuery()->getResult();
    }
    /**
     * @return Medecin[] Returns an array of Laboratoire objects
     */

    public function findByBothFieldsMed($ville,$specialite,$txt)
    {
        $query= $this->createQueryBuilder('u')
            ->from(Medecin::class,'m')
            ->select('m.prenom,m.nom,u.id,u.Ville,u.email,u.Tel,m.note')
            ->where('u.id = m.id');
        if($ville!=null && $ville!=""){
            $query->andWhere('u.Ville = :val1')
                ->setParameter('val1', $ville);
        };
        if($txt!=null && $txt!=""){
            $query->andWhere('m.nom like :val2 or m.prenom like :val2')
                ->setParameter('val2', '%'.$txt.'%');
        };
        if($specialite!=null && $specialite!=""){
            $query->andWhere(':val3 = m.specialite')
                ->setParameter('val3', $specialite);
        };

        return $query->getQuery()->getResult();
    }

    public function OrderByMailQB(){
        return $this->createQueryBuilder('s')// select
        ->orderBy('s.email','ASC')
            ->getQuery()->getResult();
    }
    // /**
    //  * @return User[] Returns an array of User objects
    //  */
    /*
    public function findByExampleField($value)
    {
        return $this->createQueryBuilder('u')
            ->andWhere('u.exampleField = :val')
            ->setParameter('val', $value)
            ->orderBy('u.id', 'ASC')
            ->setMaxResults(10)
            ->getQuery()
            ->getResult()
        ;
    }
    */

    /*
    public function findOneBySomeField($value): ?User
    {
        return $this->createQueryBuilder('u')
            ->andWhere('u.exampleField = :val')
            ->setParameter('val', $value)
            ->getQuery()
            ->getOneOrNullResult()
        ;
    }
    */
}
