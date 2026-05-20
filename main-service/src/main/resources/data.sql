INSERT INTO users (name, email, username, password, role)
VALUES
    ('Nikita Mashkarin', 'nikita@mail.com', 'nikita', '$2a$10$7QJ3QmGm9QZ8k0Qm0f0m4eX5a1Q1b8YQp8xvYkZ0lY7qz1d0mQm8G', 'ADMIN'),
    ('Ivan Petrov', 'ivan@mail.com', 'ivan', '$2a$10$7QJ3QmGm9QZ8k0Qm0f0m4eX5a1Q1b8YQp8xvYkZ0lY7qz1d0mQm8G', 'USER'),
    ('Anna Smirnova', 'anna@mail.com', 'anna', '$2a$10$7QJ3QmGm9QZ8k0Qm0f0m4eX5a1Q1b8YQp8xvYkZ0lY7qz1d0mQm8G', 'USER'),
    ('Dmitry Volkov', 'dmitry@mail.com', 'dmitry', '$2a$10$7QJ3QmGm9QZ8k0Qm0f0m4eX5a1Q1b8YQp8xvYkZ0lY7qz1d0mQm8G', 'USER'),
    ('Olga Ivanova', 'olga@mail.com', 'olga', '$2a$10$7QJ3QmGm9QZ8k0Qm0f0m4eX5a1Q1b8YQp8xvYkZ0lY7qz1d0mQm8G', 'USER');

INSERT INTO categories (name)
VALUES
    ('Музыка'),
    ('Спорт'),
    ('IT'),
    ('Образование'),
    ('Игры');

INSERT INTO locations (lat, lon)
VALUES
    (55.7558, 37.6176),
    (59.9343, 30.3351),
    (56.8389, 60.6057),
    (43.1155, 131.8855),
    (54.7104, 20.4522);

INSERT INTO events (
    title,
    annotation,
    category_id,
    description,
    event_date,
    location_id,
    paid,
    participant_limit,
    request_moderation,
    created_on,
    published_on,
    initiator_id,
    state,
    views
)
VALUES
    (
        'Java Conference',
        'Конференция по Java',
        3,
        'Большая конференция для Java разработчиков',
        '2026-06-10 12:00:00',
        1,
        false,
        100,
        true,
        NOW(),
        NOW(),
        1,
        'PUBLISHED',
        120
    ),
    (
        'Rock Concert',
        'Музыкальный концерт',
        1,
        'Большой рок концерт в центре города',
        '2026-07-01 19:00:00',
        2,
        true,
        500,
        false,
        NOW(),
        NOW(),
        2,
        'PUBLISHED',
        350
    ),
    (
        'Football Match',
        'Матч по футболу',
        2,
        'Любительский турнир по футболу',
        '2026-06-15 15:00:00',
        3,
        false,
        50,
        true,
        NOW(),
        NOW(),
        3,
        'PENDING',
        40
    ),
    (
        'GameDev Meetup',
        'Встреча разработчиков игр',
        5,
        'Обсуждение разработки игр и движков',
        '2026-08-05 18:30:00',
        4,
        false,
        80,
        true,
        NOW(),
        NOW(),
        4,
        'PUBLISHED',
        200
    ),
    (
        'Math Seminar',
        'Семинар по математике',
        4,
        'Подготовка к олимпиадам и лекции',
        '2026-09-12 10:00:00',
        5,
        false,
        40,
        true,
        NOW(),
        NOW(),
        5,
        'CANCELED',
        15
    );

INSERT INTO compilations (pinned, title)
VALUES
    (true, 'Популярные события'),
    (false, 'IT мероприятия'),
    (true, 'Музыкальные события'),
    (false, 'Спортивные события'),
    (false, 'Образовательные события');

INSERT INTO event_compilation (event_id, compilation_id)
VALUES
    (1, 1),
    (1, 2),
    (2, 1),
    (2, 3),
    (3, 4),
    (4, 2),
    (5, 5);

INSERT INTO requests (
    event_id,
    requester_id,
    status,
    created
)
VALUES
    (1, 2, 'CONFIRMED', NOW()),
    (1, 3, 'PENDING', NOW()),
    (2, 4, 'CONFIRMED', NOW()),
    (3, 5, 'REJECTED', NOW()),
    (4, 2, 'PENDING', NOW());

INSERT INTO comments (
    created_on,
    text,
    updated_on,
    author_id,
    event_id
)
VALUES
    (
        NOW(),
        'Очень интересное мероприятие!',
        NOW(),
        2,
        1
    ),
    (
        NOW(),
        'Жду начала конференции',
        NOW(),
        3,
        1
    ),
    (
        NOW(),
        'Концерт был отличным',
        NOW(),
        4,
        2
    ),
    (
        NOW(),
        'Буду участвовать',
        NOW(),
        5,
        3
    ),
    (
        NOW(),
        'Спасибо организаторам',
        NOW(),
        1,
        4
    );