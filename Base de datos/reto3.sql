-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Servidor: 127.0.0.1
-- Tiempo de generación: 23-05-2024 a las 18:28:41
-- Versión del servidor: 10.4.32-MariaDB
-- Versión de PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Base de datos: `reto3`
--

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `clasificacion`
--

CREATE TABLE `clasificacion` (
  `id` int(11) NOT NULL,
  `equipo` varchar(50) NOT NULL,
  `puntos` int(11) NOT NULL,
  `partidos_jugados` int(11) NOT NULL,
  `partidos_ganados` int(11) NOT NULL,
  `partidos_empatados` int(11) NOT NULL,
  `partidos_perdidos` int(11) NOT NULL,
  `goles_favor` int(11) NOT NULL,
  `goles_contra` int(11) NOT NULL,
  `diferencia_goles` int(11) NOT NULL,
  `año_temporada` varchar(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `clasificacion`
--

INSERT INTO `clasificacion` (`id`, `equipo`, `puntos`, `partidos_jugados`, `partidos_ganados`, `partidos_empatados`, `partidos_perdidos`, `goles_favor`, `goles_contra`, `diferencia_goles`, `año_temporada`) VALUES
(31, 'Athletic Bilbao', 14, 10, 4, 2, 4, 62, 85, -23, '2024'),
(32, 'Real Madrid', 13, 10, 4, 1, 5, 41, 47, -6, '2024'),
(33, 'Barcelona', 3, 10, 1, 0, 9, 27, 72, -45, '2024'),
(34, 'Betis', 14, 10, 4, 2, 4, 64, 93, -29, '2024'),
(35, 'Real Sociedad', 23, 10, 7, 2, 1, 128, 52, 76, '2024'),
(36, 'Atletico Madrid', 19, 10, 6, 1, 3, 117, 90, 27, '2024');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `equipo`
--

CREATE TABLE `equipo` (
  `CodEq` char(4) NOT NULL,
  `NomEq` varchar(15) NOT NULL,
  `PresEq` varchar(20) NOT NULL,
  `CantSocios` int(11) NOT NULL,
  `CodEst` char(3) NOT NULL,
  `EscudoURL` varchar(255) DEFAULT NULL,
  `Escudo` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `equipo`
--

INSERT INTO `equipo` (`CodEq`, `NomEq`, `PresEq`, `CantSocios`, `CodEst`, `EscudoURL`, `Escudo`) VALUES
('0001', 'Athletic Bilbao', 'Jon Uriarte', 50000, '1', 'Media/Equipos/Webilbao.png', NULL),
('0002', 'Atletico Madrid', 'Enrique Cerezo', 50000, '005', NULL, NULL),
('0003', 'Real Madrid', 'Florentino Pérez', 50000, '004', 'URL del escudo', NULL),
('0004', 'Real Sociedad', 'Jokin Aperribay', 50000, '006', 'URL del escudo', NULL),
('0005', 'Barcelona', 'Joan Laporta', 50000, '002', 'URL del escudo', NULL),
('0006', 'Betis', 'Ángel Haro García', 50000, '005', 'URL del escudo', NULL);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `estadio`
--

CREATE TABLE `estadio` (
  `CodEst` char(3) NOT NULL,
  `NomEst` varchar(30) NOT NULL,
  `Ciudad` varchar(10) NOT NULL,
  `Capacidad` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `estadio`
--

INSERT INTO `estadio` (`CodEst`, `NomEst`, `Ciudad`, `Capacidad`) VALUES
('002', 'Camp Nou', 'Barcelona', 105000),
('003', 'Metropolitano', 'Madrid', 70460),
('004', 'Santiago Bernabéu', 'Madrid', 85000),
('005', 'Benito Villamarín', 'Sevilla', 60271),
('006', 'Reale Arena', 'San Sebast', 39313),
('1', 'San Mames', 'Bilbao', 14000);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jornada`
--

CREATE TABLE `jornada` (
  `CodJor` char(8) NOT NULL,
  `NumJor` int(11) NOT NULL,
  `Partido1Local` varchar(15) DEFAULT NULL,
  `Partido1Visitante` varchar(15) DEFAULT NULL,
  `GolesLocalPartido1` int(11) DEFAULT NULL,
  `GolesVisitantePartido1` int(11) DEFAULT NULL,
  `Partido2Local` varchar(15) DEFAULT NULL,
  `Partido2Visitante` varchar(15) DEFAULT NULL,
  `GolesLocalPartido2` int(11) DEFAULT NULL,
  `GolesVisitantePartido2` int(11) DEFAULT NULL,
  `Partido3Local` varchar(15) DEFAULT NULL,
  `Partido3Visitante` varchar(15) DEFAULT NULL,
  `GolesLocalPartido3` int(11) DEFAULT NULL,
  `GolesVisitantePartido3` int(11) DEFAULT NULL,
  `Año` varchar(4) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jornada`
--

INSERT INTO `jornada` (`CodJor`, `NumJor`, `Partido1Local`, `Partido1Visitante`, `GolesLocalPartido1`, `GolesVisitantePartido1`, `Partido2Local`, `Partido2Visitante`, `GolesLocalPartido2`, `GolesVisitantePartido2`, `Partido3Local`, `Partido3Visitante`, `GolesLocalPartido3`, `GolesVisitantePartido3`, `Año`) VALUES
('J102024', 10, 'Atletico Madrid', 'Athletic Bilbao', 12, 4, 'Betis', 'Real Sociedad', 4, 6, 'Barcelona', 'Real Madrid', 2, 6, '2024'),
('J12024', 1, 'Betis', 'Athletic Bilbao', 12, 12, 'Barcelona', 'Real Sociedad', 1, 2, 'Real Madrid', 'Atletico Madrid', 1, 2, '2024'),
('J22024', 2, 'Athletic Bilbao', 'Real Madrid', 1, 1, 'Betis', 'Real Sociedad', 2, 2, 'Atletico Madrid', 'Barcelona', 1, 2, '2024'),
('J32024', 3, 'Athletic Bilbao', 'Atletico Madrid', 12, 2, 'Real Sociedad', 'Barcelona', 12, 2, 'Betis', 'Real Madrid', 23, 4, '2024'),
('J42024', 4, 'Athletic Bilbao', 'Barcelona', 12, 2, 'Real Sociedad', 'Atletico Madrid', 34, 4, 'Real Madrid', 'Betis', 2, 4, '2024'),
('J52024', 5, 'Real Sociedad', 'Atletico Madrid', 23, 23, 'Real Madrid', 'Athletic Bilbao', 3, 4, 'Barcelona', 'Betis', 4, 5, '2024'),
('J62024', 6, 'Real Sociedad', 'Athletic Bilbao', 23, 4, 'Barcelona', 'Real Madrid', 2, 4, 'Betis', 'Atletico Madrid', 2, 4, '2024'),
('J72024', 7, 'Real Sociedad', 'Athletic Bilbao', 12, 4, 'Atletico Madrid', 'Barcelona', 12, 4, 'Real Madrid', 'Betis', 2, 4, '2024'),
('J82024', 8, 'Atletico Madrid', 'Betis', 34, 4, 'Barcelona', 'Athletic Bilbao', 4, 5, 'Real Sociedad', 'Real Madrid', 1, 4, '2024'),
('J92024', 9, 'Real Madrid', 'Athletic Bilbao', 14, 4, 'Atletico Madrid', 'Betis', 23, 4, 'Real Sociedad', 'Barcelona', 13, 4, '2024');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugador`
--

CREATE TABLE `jugador` (
  `NumFicha` int(11) NOT NULL,
  `NomJug` varchar(12) NOT NULL,
  `ApeJug` varchar(15) NOT NULL,
  `Edad` int(11) NOT NULL,
  `Peso` float NOT NULL,
  `Altura` float NOT NULL,
  `DNIJug` char(9) DEFAULT NULL,
  `ruta_foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jugador`
--

INSERT INTO `jugador` (`NumFicha`, `NomJug`, `ApeJug`, `Edad`, `Peso`, `Altura`, `DNIJug`, `ruta_foto`) VALUES
(101, 'Luka', 'Modric', 36, 68.5, 1.74, '123456789', 'Media/Jugadores/luka_modric.png'),
(102, 'Toni', 'Kroos', 31, 78.3, 1.83, '234567890', 'Media/Jugadores/ToniKroos.jpg'),
(103, 'David', 'Alaba', 29, 79.8, 1.92, '345678901', 'Media/Jugadores/davidalaba.png'),
(104, 'Nacho', 'Fernández', 31, 76.2, 1.8, '456789012', 'Media/Jugadores/NachoFernandez.png'),
(105, 'Joselu', 'Mato', 31, 82.4, 1.87, '567890123', 'Media/Jugadores/JoseluMato.png'),
(106, 'Thibaut', 'Courtois', 29, 91.5, 1.98, '678901234', 'Media/Jugadores/ThibautCourtois.jpg'),
(107, 'Dani', 'Carvajal', 29, 73.6, 1.77, '789012345', 'Media/Jugadores/dani_carvajal.jpg'),
(108, 'Antonio', 'Rudiger', 28, 82.9, 1.86, '890123456', 'Media/Jugadores/antonio_rudiger.jpg'),
(109, 'Andriy', 'Lunin', 24, 85, 1.91, '901234567', 'Media/Jugadores/andriy_lunin.jpeg'),
(110, 'Brahim', 'Díaz', 21, 69.3, 1.73, '012345678', 'Media/Jugadores/brahim_diaz.png'),
(201, 'Claudio', 'Bravo', 38, 82.7, 1.86, '234567890', 'Media/Jugadores/claudio_bravo.jpg'),
(202, 'Aitor', 'Ruibal', 26, 76.9, 1.79, '345678901', 'Media/Jugadores/Aitor_rubial.jpeg'),
(203, 'Guido', 'Rodríguez', 27, 80.5, 1.88, '456789012', 'Media/Jugadores/Guido_Rodriguez.png'),
(204, 'William', 'Carvalho', 29, 84.3, 1.91, '567890123', 'Media/Jugadores/William_carvalho.png'),
(205, 'Sergio', 'Canales', 30, 75.6, 1.85, '678901234', 'Media/Jugadores/Sergio_canales.jpg'),
(206, 'Nabil', 'Fekir', 28, 73.2, 1.79, '789012345', 'Media/Jugadores/Nabil_fekir.png'),
(207, 'Borja', 'Iglesias', 29, 81.8, 1.88, '890123456', 'Media/Jugadores/Borja_Iglesias.jpeg'),
(208, 'Marc', 'Bartra', 30, 76.1, 1.86, '901234567', 'Media/Jugadores/Marc_Bartra.jpg'),
(209, 'Alex', 'Moreno', 27, 73.4, 1.82, '012345678', 'Media/Jugadores/Alex-Moreno.png'),
(211, 'Emerson', 'Royal', 22, 71.2, 1.75, '234567890', 'Media/Jugadores/enric_garcia.png'),
(301, 'Jan', 'Oblak', 29, 86.3, 1.88, '345678901', 'Media/Jugadores/Joao_Felix.png'),
(302, 'Koke', '', 29, 75.6, 1.79, '456789012', 'Media/Jugadores/Koke.jpg'),
(303, 'João', 'Félix', 22, 72.1, 1.81, '567890123', 'Media/Jugadores/Joao_Felix.png'),
(304, 'Luis', 'Suárez', 34, 79.4, 1.82, '678901234', 'Media/Jugadores/luis_suarez.png'),
(305, 'Thomas', 'Lemar', 26, 71.8, 1.76, '789012345', 'Media/Jugadores/thomas_lemar.jpeg'),
(306, 'Geoffrey', 'Kondogbia', 28, 83.5, 1.88, '890123456', 'Media/Jugadores/Geoggrey_Kondogbia.png'),
(307, 'Marcos', 'Llorente', 26, 77, 1.82, '901234567', 'Media/Jugadores/Marcos_llorente.png'),
(308, 'Jose', 'Giménez', 26, 80.2, 1.85, '012345678', 'Media/Jugadores/Jose_Gimenez.jpg'),
(310, 'Moussa', 'Dembele', 24, 78.9, 1.84, '234567890', 'Media/Jugadores/Moussa_Dembele.png'),
(311, 'Matheus', 'Cunha', 22, 74.3, 1.79, '345678901', 'Media/Jugadores/Matheus_Chuna.jpg'),
(401, 'Unai', 'Simón', 24, 80.2, 1.86, '456789012', 'Media/Jugadores/Unai_Simon.png'),
(402, 'Iñigo', 'Martínez', 30, 79.5, 1.84, '567890123', 'Media/Jugadores/IÑIGO_MARTÍNEZ.jpg'),
(403, 'Yeray', 'Álvarez', 26, 81.8, 1.88, '678901234', 'Media/Jugadores/Yeray_Alvarez.png'),
(404, 'Iñaki', 'Williams', 27, 78.3, 1.82, '789012345', 'Media/Jugadores/Iñaki_Wiliams.jpg'),
(405, 'Raúl', 'García', 34, 84.6, 1.89, '890123456', 'Media/Jugadores/Raul_garcia.jpg'),
(406, 'Asier', 'Villalibre', 24, 76.9, 1.83, '901234567', 'Media/Jugadores/Asier_Villalibre.jpg'),
(407, 'Oier', 'Zarraga', 23, 72.5, 1.79, '012345678', 'Media/Jugadores/Oier_Zarraga.png'),
(409, 'Mikel', 'Balenziaga', 32, 75.1, 1.78, '234567890', 'Media/Jugadores/Mikel_Balenziaga.png'),
(410, 'Iñigo', 'Córdoba', 24, 73.4, 1.77, '345678901', 'Media/Jugadores/Inigo-Cordoba.png'),
(411, 'Jon', 'Morcillo', 23, 71.8, 1.76, '456789012', 'Media/Jugadores/Jon_Morcillo.png'),
(501, 'Marc-André', 'ter Stegen', 29, 85, 1.87, '567890123', 'Media/Jugadores/Marc-Andre_Ter-Stegen.png'),
(502, 'Gerard', 'Piqué', 34, 83.7, 1.94, '678901234', 'Media/Jugadores/Gerard_Pique.jpg'),
(503, 'Sergio', 'Busquets', 32, 76.8, 1.89, '789012345', 'Media/Jugadores/Segio_busquets.jpeg'),
(504, 'Ansu', 'Fati', 19, 72.5, 1.78, '890123456', 'Media/Jugadores/Ansu_Fati.png'),
(505, 'Pedri', 'González', 19, 70.3, 1.75, '901234567', 'Media/Jugadores/Pedri_Gonzales.png'),
(506, 'Frenkie', 'de Jong', 24, 74.9, 1.81, '012345678', 'Media/Jugadores/Frenkie_deJong.png'),
(508, 'Jordi', 'Alba', 32, 74, 1.78, '234567890', 'Media/Jugadores/JORDI_ALBA-.jpg'),
(509, 'Sergi', 'Roberto', 29, 73.7, 1.76, '345678901', 'Media/Jugadores/sergi_robert.png'),
(510, 'Eric', 'García', 21, 78.2, 1.82, '456789012', 'Media/Jugadores/enric_garcia.png'),
(511, 'Ronald', 'Araújo', 22, 82.4, 1.9, '567890123', 'Media/Jugadores/Ronald_araujo.png'),
(601, 'Álex', 'Remiro', 25, 81, 1.88, '678901234', 'Media/Jugadores/Alex_Remiro.png'),
(602, 'Mikel', 'Merino', 24, 78.5, 1.85, '789012345', 'Media/Jugadores/mikel_merino.png'),
(603, 'David', 'Silva', 35, 69.8, 1.73, '890123456', 'Media/Jugadores/david_silva.jpg'),
(604, 'Alexander', 'Isak', 21, 77.2, 1.9, '901234567', 'Media/Jugadores/Alexander_Isak.png'),
(605, 'Mikel', 'Oyarzabal', 24, 76.1, 1.79, '012345678', 'Media/Jugadores/Mikel_oyarzabal.jpg'),
(607, 'Ander', 'Barrenetxea', 20, 70.2, 1.8, '234567890', 'Media/Jugadores/Ander_Barrenetxea.jpg'),
(608, 'Jon', 'Bautista', 25, 78.9, 1.85, '345678901', 'Media/Jugadores/Jon_Bautita.png'),
(609, 'Igor', 'Zubeldia', 24, 75.6, 1.82, '456789012', 'Media/Jugadores/IÑIGO_MARTÍNEZ.jpg'),
(610, 'Robin', 'Le Normand', 24, 80.4, 1.89, '567890123', 'Media/Jugadores/Robin_Normanda.png'),
(611, 'Modibo', 'Sagnan', 22, 82.1, 1.91, '678901234', 'Media/Jugadores/Modibo.png');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugador_equipo`
--

CREATE TABLE `jugador_equipo` (
  `NomEq` varchar(15) NOT NULL,
  `EscudoURL` varchar(255) DEFAULT NULL,
  `NomJug` varchar(12) NOT NULL,
  `ApeJug` varchar(15) NOT NULL,
  `ruta_foto` varchar(255) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `jugador_equipo_historial`
--

CREATE TABLE `jugador_equipo_historial` (
  `NomEq` varchar(255) DEFAULT NULL,
  `EscudoURL` varchar(255) DEFAULT NULL,
  `NomJug` varchar(255) DEFAULT NULL,
  `ApeJug` varchar(255) DEFAULT NULL,
  `ruta_foto` varchar(255) DEFAULT NULL,
  `Año` int(11) DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `jugador_equipo_historial`
--

INSERT INTO `jugador_equipo_historial` (`NomEq`, `EscudoURL`, `NomJug`, `ApeJug`, `ruta_foto`, `Año`) VALUES
('Athletic Bilbao', 'URL del escudo', 'Iñaki', 'Williams', 'ruta_williams.jpg', 2024),
('Athletic Bilbao', 'URL del escudo', 'Jon', 'Morcillo', 'ruta_morcillo.jpg', 2024),
('Athletic Bilbao', 'URL del escudo', 'Mikel', 'Balenziaga', 'ruta_balenziaga.jpg', 2024),
('Atletico Madrid', 'Gestion_Temporadas/Media/Equipos/AtMandril.png', 'David', 'Alaba', 'ruta_alaba.jpg', 2024),
('Atletico Madrid', 'Gestion_Temporadas/Media/Equipos/AtMandril.png', 'Luka', 'Modric', 'Media/Equipos/AtMandril.png', 2024),
('Atletico Madrid', 'Gestion_Temporadas/Media/Equipos/AtMandril.png', 'Toni', 'Kroos', 'ruta_kroos.jpg', 2024),
('Barcelona', 'URL del escudo', 'Thomas', 'Lemar', 'ruta_lemar.jpg', 2024),
('Barcelona', 'URL del escudo', 'Unai', 'Simón', 'ruta_simon.jpg', 2024),
('Barcelona', 'URL del escudo', 'Yeray', 'Álvarez', 'ruta_alvarez.jpg', 2024),
('Betis', 'URL del escudo', 'Asier', 'Villalibre', 'ruta_villalibre.jpg', 2024),
('Betis', 'URL del escudo', 'Iñigo', 'Martínez', 'ruta_martinez.jpg', 2024),
('Betis', 'URL del escudo', 'Raúl', 'García', 'ruta_garcia.jpg', 2024),
('Real Madrid', 'URL del escudo', 'Geoffrey', 'Kondogbia', 'ruta_kondogbia.jpg', 2024),
('Real Madrid', 'URL del escudo', 'Marcos', 'Llorente', 'ruta_llorente.jpg', 2024),
('Real Madrid', 'URL del escudo', 'Nacho', 'Fernández', 'ruta_nacho.jpg', 2024),
('Real Sociedad', 'URL del escudo', 'Jose', 'Giménez', 'ruta_gimenez.jpg', 2024),
('Real Sociedad', 'URL del escudo', 'Matheus', 'Cunha', 'ruta_cunha.jpg', 2024),
('Real Sociedad', 'URL del escudo', 'Moussa', 'Dembele', 'ruta_dembele.jpg', 2024);

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `temporada`
--

CREATE TABLE `temporada` (
  `Año` varchar(4) NOT NULL,
  `Estado` enum('Creado','Iniciado','Finalizado','actual') DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `temporada`
--

INSERT INTO `temporada` (`Año`, `Estado`) VALUES
('2024', 'Finalizado');

-- --------------------------------------------------------

--
-- Estructura de tabla para la tabla `usuario`
--

CREATE TABLE `usuario` (
  `CodUsu` int(11) NOT NULL,
  `email` varchar(30) DEFAULT NULL,
  `NomUsu` varchar(15) DEFAULT NULL,
  `Contraseña_hash` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Volcado de datos para la tabla `usuario`
--

INSERT INTO `usuario` (`CodUsu`, `email`, `NomUsu`, `Contraseña_hash`) VALUES
(2, 'ahmad1@gmail.com', 'Ahmad', 'd68d4e9d04509ce4f07c69d4dd21429d602a7b7d506688dd465fa88d965cb0e9'),
(6, 'ahmadhkary53@gmail.com', 'Ahmad', 'd65b1bfe5660e62357d21a54d52e080b18c3a448343e4df078c4f617c5f8ce1d'),
(8, 'ahmad13@gmail.com', 'Ahmad', 'd68d4e9d04509ce4f07c69d4dd21429d602a7b7d506688dd465fa88d965cb0e9');

--
-- Índices para tablas volcadas
--

--
-- Indices de la tabla `clasificacion`
--
ALTER TABLE `clasificacion`
  ADD PRIMARY KEY (`id`),
  ADD KEY `año_temporada` (`año_temporada`);

--
-- Indices de la tabla `equipo`
--
ALTER TABLE `equipo`
  ADD PRIMARY KEY (`CodEq`),
  ADD UNIQUE KEY `NomEq` (`NomEq`),
  ADD KEY `CodEst` (`CodEst`);

--
-- Indices de la tabla `estadio`
--
ALTER TABLE `estadio`
  ADD PRIMARY KEY (`CodEst`);

--
-- Indices de la tabla `jornada`
--
ALTER TABLE `jornada`
  ADD PRIMARY KEY (`CodJor`),
  ADD KEY `fk_jornada_temporada` (`Año`);

--
-- Indices de la tabla `jugador`
--
ALTER TABLE `jugador`
  ADD PRIMARY KEY (`NumFicha`),
  ADD KEY `NomJug` (`NomJug`),
  ADD KEY `ApeJug` (`ApeJug`);

--
-- Indices de la tabla `jugador_equipo`
--
ALTER TABLE `jugador_equipo`
  ADD PRIMARY KEY (`NomEq`,`NomJug`,`ApeJug`),
  ADD KEY `NomJug` (`NomJug`),
  ADD KEY `ApeJug` (`ApeJug`);

--
-- Indices de la tabla `temporada`
--
ALTER TABLE `temporada`
  ADD PRIMARY KEY (`Año`);

--
-- Indices de la tabla `usuario`
--
ALTER TABLE `usuario`
  ADD PRIMARY KEY (`CodUsu`);

--
-- AUTO_INCREMENT de las tablas volcadas
--

--
-- AUTO_INCREMENT de la tabla `clasificacion`
--
ALTER TABLE `clasificacion`
  MODIFY `id` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=37;

--
-- AUTO_INCREMENT de la tabla `usuario`
--
ALTER TABLE `usuario`
  MODIFY `CodUsu` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=9;

--
-- Restricciones para tablas volcadas
--

--
-- Filtros para la tabla `clasificacion`
--
ALTER TABLE `clasificacion`
  ADD CONSTRAINT `clasificacion_ibfk_1` FOREIGN KEY (`año_temporada`) REFERENCES `temporada` (`Año`);

--
-- Filtros para la tabla `equipo`
--
ALTER TABLE `equipo`
  ADD CONSTRAINT `equipo_ibfk_1` FOREIGN KEY (`CodEst`) REFERENCES `estadio` (`CodEst`) ON UPDATE CASCADE;

--
-- Filtros para la tabla `jornada`
--
ALTER TABLE `jornada`
  ADD CONSTRAINT `fk_jornada_temporada` FOREIGN KEY (`Año`) REFERENCES `temporada` (`Año`);

--
-- Filtros para la tabla `jugador_equipo`
--
ALTER TABLE `jugador_equipo`
  ADD CONSTRAINT `jugador_equipo_ibfk_1` FOREIGN KEY (`NomEq`) REFERENCES `equipo` (`NomEq`),
  ADD CONSTRAINT `jugador_equipo_ibfk_2` FOREIGN KEY (`NomJug`) REFERENCES `jugador` (`NomJug`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  ADD CONSTRAINT `jugador_equipo_ibfk_3` FOREIGN KEY (`ApeJug`) REFERENCES `jugador` (`ApeJug`) ON DELETE NO ACTION ON UPDATE NO ACTION;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
