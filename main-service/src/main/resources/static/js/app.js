const API_BASE = "http://localhost:8080";

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

async function getCategoryById() {

    const id =
        document.getElementById("getCategoryId").value;

    try {

        const response = await fetch(
            `${API_BASE}/categories/${id}`,
            {
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error("Категория не найдена");
        }

        const cat = await response.json();

        document.getElementById("singleCategory")
            .innerHTML = `
                <div class="event-card">
                    <h3>${cat.name}</h3>
                    <p>ID: ${cat.id}</p>
                </div>
            `;

    } catch (e) {

        document.getElementById("singleCategory")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function createCategory() {

    const body = {

        name:
        document.getElementById(
            "createCategoryName"
        ).value
    };

    try {

        const response = await fetch(
            `${API_BASE}/admin/categories`,
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

        document.getElementById(
            "createCategoryResult"
        ).innerHTML =
            `<div class="success">
            Категория создана
        </div>`;

        loadCategories();

    } catch (e) {

        document.getElementById(
            "createCategoryResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function updateCategory() {

    const id =
        document.getElementById(
            "updateCategoryId"
        ).value;

    const body = {

        name:
        document.getElementById(
            "updateCategoryName"
        ).value
    };

    try {

        const response = await fetch(
            `${API_BASE}/admin/categories/${id}`,
            {
                method: "PATCH",
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

        document.getElementById(
            "updateCategoryResult"
        ).innerHTML =
            `<div class="success">
            Категория обновлена
        </div>`;

        loadCategories();

    } catch (e) {

        document.getElementById(
            "updateCategoryResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function deleteCategory() {

    const id =
        document.getElementById(
            "deleteCategoryId"
        ).value;

    try {

        const response = await fetch(
            `${API_BASE}/admin/categories/${id}`,
            {
                method: "DELETE",
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        document.getElementById(
            "deleteCategoryResult"
        ).innerHTML =
            `<div class="success">
            Категория удалена
        </div>`;

        loadCategories();

    } catch (e) {

        document.getElementById(
            "deleteCategoryResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function createComment() {

    const eventId =
        document.getElementById("commentEventId").value;

    const body = {

        text:
        document.getElementById("commentText").value
    };

    try {

        const response = await fetch(
            `${API_BASE}/events/${eventId}/comment/`,
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

        const data = await response.json();

        document.getElementById("createCommentResult")
            .innerHTML = `
                <div class="success">
                    Комментарий создан ID: ${data.id}
                </div>
            `;

    } catch (e) {

        document.getElementById("createCommentResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function updateComment() {

    const commentId =
        document.getElementById("updateCommentId").value;

    const body = {

        text:
        document.getElementById("updateCommentText").value
    };

    try {

        const response = await fetch(
            `${API_BASE}/comment/${commentId}`,
            {
                method: "PATCH",
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

        const data = await response.json();

        document.getElementById("updateCommentResult")
            .innerHTML = `
                <div class="success">
                    Комментарий обновлен
                </div>

                <div class="event-card">
                    <p>${data.text}</p>
                </div>
            `;

    } catch (e) {

        document.getElementById("updateCommentResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function getCommentsByEventId() {

    const eventId =
        document.getElementById("commentsEventId").value;

    try {

        const response = await fetch(
            `${API_BASE}/events/${eventId}/comments`,
            {
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        const data = await response.json();

        const container =
            document.getElementById("commentsContainer");

        container.innerHTML = "";

        data.forEach(comment => {

            container.innerHTML += `
                <div class="event-card">

                    <h3>
                        Комментарий #${comment.id}
                    </h3>

                    <p>${comment.text}</p>

                    <p>
                        Создан:
                        ${comment.createdOn}
                    </p>

                    <p>
                        Обновлен:
                        ${comment.updatedOn || "-"}
                    </p>

                </div>
            `;
        });

    } catch (e) {

        document.getElementById("commentsContainer")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function getCommentById() {

    const commentId =
        document.getElementById("getCommentId").value;

    try {

        const response = await fetch(
            `${API_BASE}/comment/${commentId}`,
            {
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        const comment = await response.json();

        document.getElementById("singleComment")
            .innerHTML = `
                <div class="event-card">

                    <h3>
                        Комментарий #${comment.id}
                    </h3>

                    <p>${comment.text}</p>

                    <p>
                        Создан:
                        ${comment.createdOn}
                    </p>

                    <p>
                        Обновлен:
                        ${comment.updatedOn || "-"}
                    </p>

                </div>
            `;

    } catch (e) {

        document.getElementById("singleComment")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function deleteComment() {

    const commentId =
        document.getElementById("deleteCommentId").value;

    try {

        const response = await fetch(
            `${API_BASE}/comment/${commentId}`,
            {
                method: "DELETE",
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        document.getElementById("deleteCommentResult")
            .innerHTML = `
                <div class="success">
                    Комментарий удален
                </div>
            `;

    } catch (e) {

        document.getElementById("deleteCommentResult")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function loadCompilations() {

    const pinned =
        document.getElementById("filterPinned")?.checked;

    try {

        const response = await fetch(
            `${API_BASE}/compilations?pinned=${pinned}`,
            {
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        const data = await response.json();

        const container =
            document.getElementById("compilations");

        container.innerHTML = "";

        data.forEach(comp => {

            container.innerHTML += `
                <div class="event-card">

                    <h2>${comp.title}</h2>

                    <p>
                        ID: ${comp.id}
                    </p>

                    <p>
                        Закреплена:
                        ${comp.pinned}
                    </p>

                    <p>
                        Событий:
                        ${comp.events?.length || 0}
                    </p>

                </div>
            `;
        });

    } catch (e) {

        document.getElementById("compilations")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function getCompilationById() {

    const id =
        document.getElementById("getCompilationId").value;

    try {

        const response = await fetch(
            `${API_BASE}/compilations/${id}`,
            {
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        const comp = await response.json();

        const eventsHtml =
            comp.events?.map(event => `
                <li>
                    ${event.title}
                </li>
            `).join("") || "";

        document.getElementById("singleCompilation")
            .innerHTML = `
                <div class="event-card">

                    <h2>${comp.title}</h2>

                    <p>ID: ${comp.id}</p>

                    <p>
                        Закреплена:
                        ${comp.pinned}
                    </p>

                    <h3>События</h3>

                    <ul>
                        ${eventsHtml}
                    </ul>

                </div>
            `;

    } catch (e) {

        document.getElementById("singleCompilation")
            .innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function createCompilation() {

    const body = {

        title:
        document.getElementById(
            "createCompilationTitle"
        ).value,

        pinned:
        document.getElementById(
            "createCompilationPinned"
        ).checked,

        events:
            document.getElementById(
                "createCompilationEvents"
            ).value
                .split(",")
                .filter(x => x.trim() !== "")
                .map(Number)
    };

    try {

        const response = await fetch(
            `${API_BASE}/admin/compilations`,
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

        document.getElementById(
            "createCompilationResult"
        ).innerHTML =
            `<div class="success">
            Подборка создана
        </div>`;

        loadCompilations();

    } catch (e) {

        document.getElementById(
            "createCompilationResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function updateCompilation() {

    const id =
        document.getElementById(
            "updateCompilationId"
        ).value;

    const body = {

        title:
        document.getElementById(
            "updateCompilationTitle"
        ).value,

        pinned:
        document.getElementById(
            "updateCompilationPinned"
        ).checked,

        events:
            document.getElementById(
                "updateCompilationEvents"
            ).value
                .split(",")
                .filter(x => x.trim() !== "")
                .map(Number)
    };

    try {

        const response = await fetch(
            `${API_BASE}/admin/compilations/${id}`,
            {
                method: "PATCH",
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

        document.getElementById(
            "updateCompilationResult"
        ).innerHTML =
            `<div class="success">
            Подборка обновлена
        </div>`;

        loadCompilations();

    } catch (e) {

        document.getElementById(
            "updateCompilationResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

async function deleteCompilation() {

    const id =
        document.getElementById(
            "deleteCompilationId"
        ).value;

    try {

        const response = await fetch(
            `${API_BASE}/admin/compilations/${id}`,
            {
                method: "DELETE",
                credentials: "include"
            }
        );

        if (!response.ok) {

            throw new Error(await response.text());
        }

        document.getElementById(
            "deleteCompilationResult"
        ).innerHTML =
            `<div class="success">
            Подборка удалена
        </div>`;

        loadCompilations();

    } catch (e) {

        document.getElementById(
            "deleteCompilationResult"
        ).innerHTML =
            `<div class="error">${e.message}</div>`;
    }
}

// =========================
// EVENTS
// =========================

async function getPublishedEvents() {

    const response = await fetch(
        `${API_BASE}/events`,
        {
            credentials: "include"
        }
    );

    const data = await response.json();

    console.log(data);

    return data;
}

async function getPublishedEventById(eventId) {

    const response = await fetch(
        `${API_BASE}/events/${eventId}`,
        {
            credentials: "include"
        }
    );

    return await response.json();
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

    return await response.json();
}

async function getUserEvents() {

    const response = await fetch(
        `${API_BASE}/events?from=0&size=10`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function getUserEventById(eventId) {

    const response = await fetch(
        `${API_BASE}/events/${eventId}`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function updateUserEvent(eventId, body) {

    const response = await fetch(
        `${API_BASE}/events/${eventId}`,
        {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }
    );

    return await response.json();
}

// =========================
// ADMIN EVENTS
// =========================

async function getAdminEvents() {

    const response = await fetch(
        `${API_BASE}/admin/events`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function updateAdminEvent(eventId, body) {

    const response = await fetch(
        `${API_BASE}/admin/events/${eventId}`,
        {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }
    );

    return await response.json();
}

// =========================
// REQUESTS
// =========================

async function createParticipationRequest(eventId) {

    const response = await fetch(
        `${API_BASE}/requests?eventId=${eventId}`,
        {
            method: "POST",
            credentials: "include"
        }
    );

    return await response.json();
}

async function cancelParticipationRequest(requestId) {

    const response = await fetch(
        `${API_BASE}/requests/${requestId}/cancel`,
        {
            method: "PATCH",
            credentials: "include"
        }
    );

    return await response.json();
}

async function getParticipationRequests() {

    const response = await fetch(
        `${API_BASE}/requests`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function getParticipationRequestsForUserEvent(eventId) {

    const response = await fetch(
        `${API_BASE}/events/${eventId}/requests`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function changeParticipationRequestsStatus(
    eventId,
    requestIds,
    status
) {

    const body = {
        requestIds,
        status
    };

    const response = await fetch(
        `${API_BASE}/events/${eventId}/requests`,
        {
            method: "PATCH",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }
    );

    return await response.json();
}

// =========================
// USERS
// =========================

async function getUsers() {

    const response = await fetch(
        `${API_BASE}/users`,
        {
            credentials: "include"
        }
    );

    return await response.json();
}

async function createUser(body) {

    const response = await fetch(
        `${API_BASE}/auth/register`,
        {
            method: "POST",
            credentials: "include",
            headers: {
                "Content-Type": "application/json"
            },
            body: JSON.stringify(body)
        }
    );

    return await response.json();
}

async function deleteUser(userId) {

    await fetch(
        `${API_BASE}/users/${userId}`,
        {
            method: "DELETE",
            credentials: "include"
        }
    );
}
