const API_BASE = "";

async function login() {

    const formData = new URLSearchParams();

    formData.append(
        "username",
        document.getElementById("loginUsername").value
    );

    formData.append(
        "password",
        document.getElementById("loginPassword").value
    );

    try {

        const response = await fetch(
            `${API_BASE}/login`,
            {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type":
                        "application/x-www-form-urlencoded"
                },
                body: formData
            }
        );

        if (response.ok || response.redirected) {

            document.getElementById("loginResult")
                .innerHTML =
                `<div class="success">
                    Успешный вход
                </div>`;

            setTimeout(() => {
                location.href = "events.html";
            }, 1000);

        } else {

            throw new Error("Ошибка входа");
        }

    } catch (e) {

        document.getElementById("loginResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function register() {

    const body = {

        name:
        document.getElementById("regName").value,

        email:
        document.getElementById("regEmail").value,

        username:
        document.getElementById("regUsername").value,

        password:
        document.getElementById("regPassword").value
    };

    try {

        const response = await fetch(
            `${API_BASE}/users`,
            {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        document.getElementById("registerResult")
            .innerHTML =
            `<div class="success">
                Регистрация успешна
            </div>`;

    } catch (e) {

        document.getElementById("registerResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function loadCategories() {

    const response = await fetch(
        `${API_BASE}/categories`,
        {
            credentials: "include"
        }
    );

    const data = await response.json();

    const container =
        document.getElementById("categories");

    container.innerHTML = "";

    data.forEach(cat => {

        container.innerHTML += `
            <div class="event-card">
                <h3>${cat.name}</h3>
                <p>ID: ${cat.id}</p>
            </div>
        `;
    });
}

async function loadEvents() {

    const response = await fetch(
        `${API_BASE}/events`,
        {
            credentials: "include"
        }
    );

    const data = await response.json();

    const container =
        document.getElementById("events");

    container.innerHTML = "";

    data.forEach(event => {

        container.innerHTML += `
            <div class="event-card">

                <h3>${event.title}</h3>

                <p>${event.annotation}</p>

                <p>
                    <strong>Дата:</strong>
                    ${event.eventDate}
                </p>

                <p>
                    <strong>Категория:</strong>
                    ${event.category?.name || "-"}
                </p>

            </div>
        `;
    });
}

async function createEvent() {

    const body = {

        title:
        document.getElementById("eventTitle").value,

        annotation:
        document.getElementById("eventAnnotation").value,

        description:
        document.getElementById("eventDescription").value,

        category:
            Number(
                document.getElementById("eventCategory").value
            ),

        eventDate:
            new Date(
                document.getElementById("eventDate").value
            ).toISOString(),

        location: {

            lat:
                Number(
                    document.getElementById("eventLat").value
                ),

            lon:
                Number(
                    document.getElementById("eventLon").value
                )
        },

        paid: false,
        participantLimit: 10,
        requestModeration: true
    };

    try {

        const response = await fetch(
            `${API_BASE}/events`,
            {
                method: "POST",
                credentials: "include",
                headers: {
                    "Content-Type": "application/json"
                },
                body: JSON.stringify(body)
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        document.getElementById("eventResult")
            .innerHTML =
            `<div class="success">
                Событие создано
            </div>`;

    } catch (e) {

        document.getElementById("eventResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}