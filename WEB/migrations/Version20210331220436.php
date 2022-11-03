<?php

declare(strict_types=1);

namespace DoctrineMigrations;

use Doctrine\DBAL\Schema\Schema;
use Doctrine\Migrations\AbstractMigration;

/**
 * Auto-generated Migration: Please modify to your needs!
 */
final class Version20210331220436 extends AbstractMigration
{
    public function getDescription() : string
    {
        return '';
    }

    public function up(Schema $schema) : void
    {
        // this up() migration is auto-generated, please modify it to your needs
        $this->addSql('CREATE TABLE analyse (id INT AUTO_INCREMENT NOT NULL, dossier_lab_id INT NOT NULL, description LONGTEXT NOT NULL, date DATETIME NOT NULL, INDEX IDX_351B0C7E8AF847FC (dossier_lab_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('CREATE TABLE dossier_lab (id INT AUTO_INCREMENT NOT NULL, laboratoire_id INT NOT NULL, patient_id INT NOT NULL, INDEX IDX_886B319A76E2617B (laboratoire_id), INDEX IDX_886B319A6B899279 (patient_id), PRIMARY KEY(id)) DEFAULT CHARACTER SET utf8mb4 COLLATE `utf8mb4_unicode_ci` ENGINE = InnoDB');
        $this->addSql('ALTER TABLE analyse ADD CONSTRAINT FK_351B0C7E8AF847FC FOREIGN KEY (dossier_lab_id) REFERENCES dossier_lab (id)');
        $this->addSql('ALTER TABLE dossier_lab ADD CONSTRAINT FK_886B319A76E2617B FOREIGN KEY (laboratoire_id) REFERENCES laboratoire (id)');
        $this->addSql('ALTER TABLE dossier_lab ADD CONSTRAINT FK_886B319A6B899279 FOREIGN KEY (patient_id) REFERENCES patient (id)');
    }

    public function down(Schema $schema) : void
    {
        // this down() migration is auto-generated, please modify it to your needs
        $this->addSql('ALTER TABLE analyse DROP FOREIGN KEY FK_351B0C7E8AF847FC');
        $this->addSql('DROP TABLE analyse');
        $this->addSql('DROP TABLE dossier_lab');
    }
}
