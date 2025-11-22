document.addEventListener("DOMContentLoaded", () => {
  const userName = localStorage.getItem("loggedUserName");
  if (userName) {
    const profileBtn = document.querySelector(".profile-btn");
    if (profileBtn) profileBtn.textContent = userName;
  }
});

if (!document.getElementById("successPopup")) {
        console.warn("Popup element missing from DOM when loaded!");
    }

function hideAllModals() {
    const registerModal = document.getElementById("registerModal");
    const loginModal = document.getElementById("loginModal");
    if (registerModal) registerModal.classList.add("hidden");
    if (loginModal) loginModal.classList.add("hidden");
}

function toggleProfileMenu() {
    const dropdown = document.getElementById("profileDropdown");
    dropdown.classList.toggle("hidden");
}

function openModal(id) {
    hideAllModals(); // Close any open modals first
    const modal = document.getElementById(id);
    modal.classList.remove("hidden");
    document.body.style.overflow = "hidden"; // Disable scrolling while modal open
}

function closeModal(id) {
    const modal = document.getElementById(id);
    modal.classList.add("hidden");
    document.body.style.overflow = "auto"; // Re-enable scrolling
}

async function loginUser(e) {
    e.preventDefault();

    const email = document.getElementById("loginEmail").value;
    const password = document.getElementById("loginPassword").value;



    const res = await fetch("/api/users/login", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ email, password })
    });

    let data = {};
    let message = "Something went wrong";

    try {
        data = await res.json();
        message = data.message || message;
    } catch {
        message = await res.text(); // fallback for plain-text responses
    }

    if (res.ok && message.toLowerCase().includes("success")) {
        // ✅ Save logged user details
        localStorage.setItem("loggedUserId", data.userId);
        localStorage.setItem("loggedUserEmail", data.email);
        localStorage.setItem("loggedUserName", data.userName);
        localStorage.setItem("loggedUserRole", data.role);
        showPopup("Login Successful", message);
        setTimeout(() => window.location.href = "/events", 1000);
    } else {
        showPopup("Login Failed", message || "Invalid credentials.");
    }
}



function checkAdminRole() {
    const role = localStorage.getItem("loggedUserRole");

    if (!role) {
        alert("Please login first.");
        return;
    }

    if (role === "ADMIN") {
        window.location.href = "/create-event";
    } else {
        alert("Access denied: Only admins can create events.");
    }
}


function filterEvents() {
    const input = document.getElementById("searchInput").value.toLowerCase();
    const cards = document.getElementsByClassName("event-card");

    for (let card of cards) {
        const title = card.querySelector("h3").textContent.toLowerCase();
        card.style.display = title.includes(input) ? "block" : "none";
    }
}

function showPopup(title, message) {
  let popup = document.getElementById("successPopup");
  if (!popup) {
    console.error("Popup element not found in DOM");
    return;
  }

  document.getElementById("popupTitle").innerText = title;
  document.getElementById("popupMessage").innerText = message;

  popup.classList.remove("hidden");
}

function closePopup() {
  let popup = document.getElementById("successPopup");
  popup.classList.add("hidden");
}

async function registerForEvent(button) {
  const eventId = button.getAttribute('data-event-id');
  const userId = localStorage.getItem('loggedUserId');

  if (!userId) {
    showPopup("Login Required", "Please log in to register for events.");
    return;
  }

  // Disable button while request is in progress
  button.disabled = true;
  button.innerText = "Registering...";

  try {
    const res = await fetch(`/api/registrations/${eventId}?userId=${userId}`, {
      method: 'POST'
    });

    const message = await res.text();

    if (message.includes("successfully")) {
      // ✅ Registration successful
      showPopup("Registration Successful", message);
      button.innerText = "Registered ✅";
      button.classList.add("registered-btn");
      button.disabled = true;
    } else if (message.includes("already")) {
      // 🚫 Already registered
      showPopup("Already Registered", message);
      button.innerText = "Registered ✅";
      button.classList.add("registered-btn");
      button.disabled = true;
    } else {
      // ❌ Unexpected error
      showPopup("Error", "Something went wrong. Please try again.");
      button.disabled = false;
      button.innerText = "Register";
    }
  } catch (err) {
    showPopup("Error", "Server connection failed.");
    console.error(err);
    button.disabled = false;
    button.innerText = "Register";
  }
}

function logoutUser() {
    localStorage.removeItem("loggedUserId");
    localStorage.removeItem("loggedUserEmail");
    localStorage.removeItem("loggedUserName");
    localStorage.removeItem("loggedUserRole");
    window.location.href = "/";
}

async function loadMyRegistrations() {
    const userId = localStorage.getItem("loggedUserId");

    if (!userId) {
        showPopup("Login Required", "Please log in to view your events.");
        return;
    }

    const res = await fetch(`/api/registrations/user/${userId}`);
    const registrations = await res.json();

    const container = document.getElementById("registrationList");
    container.innerHTML = "";

    if (registrations.length === 0) {
        container.innerHTML = "<p class='no-events'>You have not registered for any events.</p>";
        return;
    }

    registrations.forEach(reg => {

        const start = new Date(reg.event.startTime);
        const end = new Date(reg.event.endTime);

        const startFormatted = start.toLocaleString('en-IN', {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            hour12: true
        });

        const endFormatted = end.toLocaleString('en-IN', {
            day: "2-digit",
            month: "short",
            year: "numeric",
            hour: "2-digit",
            minute: "2-digit",
            hour12: true
        });

        const card = document.createElement("div");
        card.classList.add("event-card", "glass");

        card.innerHTML = `
            <h3>${reg.event.eventTitle}</h3>
            <p>${reg.event.eventDescription}</p>

            <p><strong>Start:</strong> ${startFormatted}</p>
            <p><strong>End:</strong> ${endFormatted}</p>

            <button class="btn danger-btn" onclick="cancelRegistration(${reg.registrationId})">
                Cancel Registration
            </button>
        `;

        container.appendChild(card);
    });
}


async function cancelRegistration(id) {
    const res = await fetch(`/api/registrations/${id}`, { method: "DELETE" });

    if (res.ok) {
        showPopup("Cancelled", "Your registration was cancelled.");
        setTimeout(() => location.reload(), 1000);
    } else {
        showPopup("Error", "Failed to cancel registration.");
    }
}
