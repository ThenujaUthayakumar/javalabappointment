-- phpMyAdmin SQL Dump
-- version 4.9.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1:3308
-- Generation Time: Mar 22, 2024 at 06:27 PM
-- Server version: 8.0.18
-- PHP Version: 7.3.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `abclaboratory`
--

-- --------------------------------------------------------

--
-- Table structure for table `appointments`
--

DROP TABLE IF EXISTS `appointments`;
CREATE TABLE IF NOT EXISTS `appointments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `age` varchar(255) DEFAULT NULL,
  `appointment_date_time` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `doctor_name` varchar(255) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `gender` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `test_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_ifx2ol00nargrmxpv4ph4wvi4` (`test_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `appointments`
--

INSERT INTO `appointments` (`id`, `address`, `age`, `appointment_date_time`, `created_at`, `doctor_name`, `email`, `gender`, `name`, `phone_number`, `reference_number`, `updated_at`, `test_id`) VALUES
(1, 'Colombo', '28', '2024-03-19T10:30', '2024-03-18 23:41:57.273281', 'Bandara', 'shahrukh.thenu@gmail.com', 'Male', 'Virat', '0768908768', 'ARN1', '2024-03-18 23:41:57.273281', 1),
(2, 'Colombo', '27', '2024-03-20T10:00', '2024-03-22 19:56:52.801273', 'Kavishna', 'karthik@gmail.com', 'Male', 'Karthik', '0768902345', 'ARN2', '2024-03-22 19:56:52.801273', 2),
(3, 'Colombo', '25', '2024-03-21T09:30', '2024-03-22 20:30:19.867409', 'Ladchana', 'thenuthaya27@gmail.com', 'Female', 'Thenuja', '0762313456', 'ARN3', '2024-03-22 20:30:19.867409', 3);

-- --------------------------------------------------------

--
-- Table structure for table `contacts`
--

DROP TABLE IF EXISTS `contacts`;
CREATE TABLE IF NOT EXISTS `contacts` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `message` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `contacts`
--

INSERT INTO `contacts` (`id`, `address`, `created_at`, `message`, `name`, `phone_number`, `updated_at`) VALUES
(1, 'Colombo', '2024-03-18 22:54:07.146162', 'Hello,\nCan I know the tests lists?', 'Karthik', '071234567', '2024-03-18 22:54:07.146162'),
(2, 'Vavuniya', '2024-03-18 22:58:02.100190', 'Hello,\nDo you have any branch in Vavuniya ?', 'Vimal', '0765443234', '2024-03-18 22:58:02.100190');

-- --------------------------------------------------------

--
-- Table structure for table `lab_reports`
--

DROP TABLE IF EXISTS `lab_reports`;
CREATE TABLE IF NOT EXISTS `lab_reports` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `created_at` datetime(6) DEFAULT NULL,
  `attachment` json DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `technician_id` int(11) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `appointment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_idx6w1jk1pgh86nhslx268m3k` (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `lab_reports`
--

INSERT INTO `lab_reports` (`id`, `created_at`, `attachment`, `reference_number`, `status`, `technician_id`, `updated_at`, `appointment_id`) VALUES
(1, '2024-03-22 20:16:46.418759', '{\"filePath\": \"6ad2a2d9-d138-4a51-bc3d-c5d3425c8c24_CBC-sample-report.pdf\"}', 'REPORT1', 1, 1, '2024-03-22 20:16:46.418759', 1),
(2, '2024-03-22 20:19:34.761833', '{\"filePath\": \"fcece985-0f18-4d7a-8853-f98aff12ad0a_MRI.pdf\"}', 'REPORT2', 1, 2, '2024-03-22 20:19:34.761833', 2);

-- --------------------------------------------------------

--
-- Table structure for table `payments`
--

DROP TABLE IF EXISTS `payments`;
CREATE TABLE IF NOT EXISTS `payments` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `amount` int(11) DEFAULT NULL,
  `card_holder_name` varchar(255) DEFAULT NULL,
  `card_holder_phone_number` varchar(255) DEFAULT NULL,
  `card_number` bigint(20) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `cvv` int(11) DEFAULT NULL,
  `expiry_date` varchar(255) DEFAULT NULL,
  `reference_number` varchar(255) DEFAULT NULL,
  `status` int(11) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `appointment_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UK_2kxb37oip0md9ggekjbjmana4` (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `payments`
--

INSERT INTO `payments` (`id`, `amount`, `card_holder_name`, `card_holder_phone_number`, `card_number`, `created_at`, `cvv`, `expiry_date`, `reference_number`, `status`, `updated_at`, `appointment_id`) VALUES
(1, 1800, 'Virat', '0768908768', 2345, '2024-03-18 23:48:55.131092', 345, '2025-02', 'INVOICE1', 1, '2024-03-18 23:48:55.131092', 1),
(2, 2500, 'Karthik', '0768902345', 3452, '2024-03-22 19:57:26.830375', 342, '2024-04', 'INVOICE2', 1, '2024-03-22 19:57:26.830375', 2),
(3, 3000, 'Thenuja', '0762313456 ', 2345, '2024-03-22 20:31:33.755763', 345, '2025-03', 'INVOICE3', 1, '2024-03-22 20:31:33.755763', 3);

-- --------------------------------------------------------

--
-- Table structure for table `system_users`
--

DROP TABLE IF EXISTS `system_users`;
CREATE TABLE IF NOT EXISTS `system_users` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `role` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  `username` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `system_users`
--

INSERT INTO `system_users` (`id`, `address`, `created_at`, `email`, `name`, `password`, `phone_number`, `role`, `updated_at`, `username`) VALUES
(1, 'Jaffna', '2024-03-18 16:37:16.366525', 'thenuthaya27@gmail.com', 'Thenuja Uthayakumar', '$2a$10$GLMMp32HbgfrPyVRpGQBTePRFfoX8s6LfJ2iFV2HZE2tYDkVzq2wS', '0771234567', 'Admin', '2024-03-18 16:37:16.366525', 'Thenu');

-- --------------------------------------------------------

--
-- Table structure for table `technicians`
--

DROP TABLE IF EXISTS `technicians`;
CREATE TABLE IF NOT EXISTS `technicians` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `address` varchar(255) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `image` json DEFAULT NULL,
  `join_date` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `phone_number` varchar(255) DEFAULT NULL,
  `position` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `technicians`
--

INSERT INTO `technicians` (`id`, `address`, `created_at`, `email`, `image`, `join_date`, `name`, `phone_number`, `position`, `updated_at`) VALUES
(1, 'Colombo', '2024-03-18 21:37:20.744515', 'taruni@gmail.com', '{\"filePath\": \"87be5071-6171-4591-ba7e-4743eb537406_Laboratory technician.jpeg\"}', '2024-02-01', 'Taruni Ratnayaka', '0771234567', 'Radiology Technician', '2024-03-18 21:37:20.744515'),
(2, 'Jaffna', '2024-03-18 21:38:19.687472', 'tamil@gmail.com', '{\"filePath\": \"e42ab1a4-4f81-4ebd-8a34-87a3fa67ab28_Hombre de tiro medio sosteniendo matraz erlenmeyer _ Foto Gratis.jpeg\"}', '2024-02-09', 'Tamilnilavan', '0767890976', 'Blood Bank Technician', '2024-03-18 21:38:19.687472'),
(3, 'Vavuniya', '2024-03-18 21:39:47.032359', 'vishmi@gmail.com', '{\"filePath\": \"3571b33a-7787-4c4f-8317-25374b9b414f_Ensaio Externo - Farmácia - Iespes  - Laboratório Iespes.jpeg\"}', '2024-03-01', 'Vishmitha', '0789078908', 'Urinalysis Technician', '2024-03-18 21:39:47.032359');

-- --------------------------------------------------------

--
-- Table structure for table `tests`
--

DROP TABLE IF EXISTS `tests`;
CREATE TABLE IF NOT EXISTS `tests` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `code` varchar(255) DEFAULT NULL,
  `cost` int(11) DEFAULT NULL,
  `created_at` datetime(6) DEFAULT NULL,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `updated_at` datetime(6) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

--
-- Dumping data for table `tests`
--

INSERT INTO `tests` (`id`, `code`, `cost`, `created_at`, `description`, `name`, `updated_at`) VALUES
(1, 'TEST1', 1800, '2024-03-18 16:04:23.323152', 'Complete Blood Count', 'CBC', '2024-03-18 16:04:23.323152'),
(2, 'TEST2', 2500, '2024-03-18 16:04:49.402229', 'Magnetic Resonance Imaging', 'MRI', '2024-03-18 16:04:49.402229'),
(3, 'TEST3', 3000, '2024-03-18 16:05:14.873929', 'for bones and some soft tissues', 'X-ray', '2024-03-18 16:05:14.873929'),
(4, 'TEST4', 3500, '2024-03-18 16:05:44.615346', 'Computed Tomography', 'CT Scan', '2024-03-18 16:05:44.615346');

--
-- Constraints for dumped tables
--

--
-- Constraints for table `appointments`
--
ALTER TABLE `appointments`
  ADD CONSTRAINT `FKcydhopxbcv8860dtvp9s1lc4p` FOREIGN KEY (`test_id`) REFERENCES `tests` (`id`);

--
-- Constraints for table `lab_reports`
--
ALTER TABLE `lab_reports`
  ADD CONSTRAINT `FK9vdcyy947tall03q4ds6io49p` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`);

--
-- Constraints for table `payments`
--
ALTER TABLE `payments`
  ADD CONSTRAINT `FK9a0odew03qao7nlbdsesrux5u` FOREIGN KEY (`appointment_id`) REFERENCES `appointments` (`id`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
