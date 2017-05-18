SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "-03:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `thomas`
--
CREATE DATABASE IF NOT EXISTS `thomas` DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;
USE `thomas`;

-- 
-- Role
-- 
INSERT INTO
    `system_role` (`id`, `name`)
VALUES
    ('1', 'ADMIN'),
    ('2', 'USER');

-- 
-- User
-- 
INSERT INTO
    `system_user` (`id`, `name`, `email`, `password`, `active`, `role_id`)
VALUES
    ('1', 'Administrador', 'manoel.carvalho.neto@gmail.com', '52d9b6388eb4c67b59f2080ea27cee4', '1', '1');

Commit;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
