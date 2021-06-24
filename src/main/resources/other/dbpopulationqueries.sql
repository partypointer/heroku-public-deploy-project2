INSERT INTO Band(nome, anno_formazione, anno_scioglimento, descrizione, genere)
VALUES ('Metallica', 1981, NULL, 'Con piu'' di 100 milioni di dischi venduti, di cui 60 milioni nei soli Stati Uniti d''America, il gruppo e'' annoverato come una delle formazioni di maggior successo nella storia dell''heavy metal e del rock contemporaneo.', 'Heavy metal'),
('Noel Gallagher''s High Flying Birds', 2010, NULL, 'Gruppo alternative rock inglese fondato da Noel Gallagher nel 2010, dopo lo scioglimento degli Oasis.', 'Alternative Rock'),
('Foo Fighters', 1996, NULL, 'Nato dopo lo scioglimento dei Nirvana, il gruppo deriva il proprio nome dal termine foo fighter, espressione risalente alla WWII per indicare avvistamenti aerei riferiti da alcuni piloti alleati, simili a quelli che generalmente vengono chiamati UFO.', 'Hard Rock'),
('Pink Floyd', 1965, 2007, NULL, 'Rock');

INSERT INTO Concerto (nome, descrizione, data_inizio, indirizzo_location, icona_link)
VALUES ('Metallica: IS BACK', 'Sono tornati nella nostra capitale!', '2022-01-15', 'Palazzo dello Sport (EUR), Roma', 'https://hungarytoday.hu/wp-content/uploads/2018/04/20180405_231018_7549_1038538.jpeg'),
('High Flying City', 'Concerto alternativo nell''auditorium.', '2021-08-10', 'Auditorium Parco della Musica, Roma', 'https://nghfb10.noelgallagher.com/wp-content/uploads/2021/05/Bookcover@2x-copy-1.png'),
('Foo Fun', 'Per sentire la migliore musica all''aperto...', '2022-02-24', 'Stadio Flaminio, Roma', 'https://bassmagazine.com/.image/ar_16:9%2Cc_fill%2Ccs_srgb%2Cfl_progressive%2Cq_auto:good%2Cw_1200/MTgxNDMxMTYzOTc2NDI3NDY3/foofighterslive.jpg'),
('III Myths', 'Vuoi vivere l''esperienza musicale piu'' incredibile ascoltando tre band leggendarie? In tal caso, questo evento fa'' al caso tuo!', '2022-05-24', 'Stadio Flaminio, Roma', 'https://www.music-man.com/assets/images/instruments/expanded/john-petrucci/majesty-collection-desktop.jpg.pagespeed.ce.s1bvaMN0af.jpg'),
('The Momentary Lapse Of Reason', '...', '1988-07-11', 'Stadio Flaminio, Roma', 'https://www.ilmessaggero.it/photos/MED/43/67/4644367_2103_pink9.jpg');

INSERT INTO Tipologia_Posto (nome, descrizione, max_posti_reali_disponibili, percentuale_overbooking, prezzo_unitario, concerto_id)
VALUES ('Platinum', 'Posto accanto al palco, potrai entrare prima di tutti saltando la fila!', 50, 5, 104, 1),
('Standard', 'Posto standard', 800, 10, 75, 1),
('VIP', 'Posti distante pochi metri dal palco, potrai visitare l''artista nel backstage!', 250, 5, 50, 2),
('Primo Spalco', 'Posti sul primo spalco', 400, 8, 96, 2),
('Secondo Spalco', 'Posti sul secondo Spalco', 600, 20, 62, 2),
('Standard', 'Posto in piedi nel parco, obbligatorio il mantenimento della distanza di sicurezza di almeno 1 metro', 350, 20, 70, 3),
('SUPER Fan!', 'Posti vicino al palco', 600, 0, 62, 4),
('Priority', 'Posti standard, potrai saltare la fila e avrai una maglietta + portachiavi III Myths in regalo!', 100, 5, 62, 4),
('Standard', 'Posti sul secondo Spalco', 600, 10, 62, 4);

INSERT INTO Partecipazione(concerto_id, band_id)
VALUES (1, 1), (2, 2), (3, 3), (4, 1), (4, 2), (4, 3);

SELECT * FROM Band;
SELECT * FROM Concerto;
SELECT * FROM Tipologia_Posto;
SELECT * FROM Partecipazione;

/* VVV Da fare nel sito: (lato utente) VVV */
INSERT INTO User
INSERT INTO Account
INSERT INTO Biglietto

/* VVV PER IMPOSTARE UN UTENTE COME AMMINISTRATORE VVV */

UPDATE Account
SET Ruolo = 'ADMIN'
WHERE Id = 1;