MERGE INTO genre (genre_id, name) VALUES 
    (0, 'Комедия'), 
    (1, 'Драма'), 
    (2, 'Мультфильм'),
    (3, 'Триллер'), 
    (4, 'Документальный'), 
    (5, 'Боевик'); 
    
MERGE INTO mpa_rating (mpa_rating_id, name) VALUES 
    (0, 'G'), 
    (1, 'PG'), 
    (2, 'PG-13'),
    (3, 'R'), 
    (4, 'NC-17');