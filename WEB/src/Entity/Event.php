<?php

namespace App\Entity;

use App\Repository\EventRepository;
use Doctrine\ORM\Mapping as ORM;

/**
 * @ORM\Entity(repositoryClass=EventRepository::class)
 */
class Event
{
    /**
     * @ORM\Id
     * @ORM\GeneratedValue
     * @ORM\Column(type="integer")
     */
    private $id;

    /**
     * @ORM\Column(type="string", length=255)
     */
    private $title;

    /**
     * @ORM\Column(type="datetime")
     */
    private $start;

    /**
     * @ORM\Column(type="datetime")
     */
    private $end;

    /**
     * @ORM\Column(type="text", nullable=true)
     */
    private $description;

    /**
     * @ORM\Column(type="boolean")
     */
    private $dayoff;

    /**
     * @ORM\ManyToOne(targetEntity=laboratoire::class, inversedBy="events")
     */
    private $laboratoire;

    /**
     * @ORM\ManyToOne(targetEntity=medecin::class, inversedBy="events")
     */
    private $medecin;

    /**
     * @ORM\Column(type="integer", nullable=true)
     */
    private $rendezvous;
    
    public function getId(): ?int
    {
        return $this->id;
    }

    public function getTitle(): ?string
    {
        return $this->title;
    }

    public function setTitle(string $title): self
    {
        $this->title = $title;

        return $this;
    }

    public function getStart(): ?\DateTimeInterface
    {
        return $this->start;
    }

    public function setStart(\DateTimeInterface $start): self
    {
        $this->start = $start;

        return $this;
    }

    public function getEnd(): ?\DateTimeInterface
    {
        return $this->end;
    }

    public function setEnd(\DateTimeInterface $end): self
    {
        $this->end = $end;

        return $this;
    }

    public function getDescription(): ?string
    {
        return $this->description;
    }

    public function setDescription(?string $description): self
    {
        $this->description = $description;

        return $this;
    }

    public function getDayoff(): ?bool
    {
        return $this->dayoff;
    }

    public function setDayoff(bool $dayoff): self
    {
        $this->dayoff = $dayoff;

        return $this;
    }

    public function getLaboratoire(): ?laboratoire
    {
        return $this->laboratoire;
    }

    public function setLaboratoire(?laboratoire $laboratoire): self
    {
        $this->laboratoire = $laboratoire;

        return $this;
    }

    public function getMedecin(): ?medecin
    {
        return $this->medecin;
    }

    public function setMedecin(?medecin $medecin): self
    {
        $this->medecin = $medecin;

        return $this;
    }

    public function getRendezvous(): ?int
    {
        return $this->rendezvous;
    }

    public function setRendezvous(?int $rendezvous): self
    {
        $this->rendezvous = $rendezvous;

        return $this;
    }

}
