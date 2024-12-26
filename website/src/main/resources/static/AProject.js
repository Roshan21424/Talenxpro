const header = document.querySelector("[data-header]");

const headerActive = function () {
  if (window.scrollY > 100) {
    header.classList.add("active");
  } else {
    header.classList.remove("active");
  }
};

// Listen for scroll events and trigger the headerActive function
window.addEventListener("scroll", headerActive);

document.querySelector(".search-btn").addEventListener("click", function () {
  alert("Search feature is currently not implemented.");
});
const panels = document.querySelectorAll(".panel");

// optional Changing cards
const links = document.querySelectorAll(".optional-sidebar a");
const cards = document.querySelectorAll(".optional-card");

links.forEach((link) => {
  link.addEventListener("click", function (event) {
    event.preventDefault(); // Prevents the page from redirecting or reloading

    links.forEach((l) => l.classList.remove("optional-active"));
    this.classList.add("optional-active");

    cards.forEach((card) => card.classList.remove("optional-active"));

    const cardId = this.id + "-card";
    document.getElementById(cardId).classList.add("optional-active");
  });
});

// steps
window.addEventListener("load", () => {
  const steps = document.querySelectorAll(".steps-list-opps li");

  steps.forEach((step, index) => {
    setTimeout(() => {
      step.style.opacity = "1";
      step.style.transform = "translateY(0)";
    }, 1900 * index);
  });
});


// Expanding cards
panels.forEach((panel) => {
  panel.addEventListener("click", () => {
    removeActiveClasses();
    panel.classList.add("active");
  });
});

function removeActiveClasses() {
  panels.forEach((panel) => {
    panel.classList.remove("active");
  });
}

// domains
function toggleSubdomainsNew(domainId) {
  const subdomainsDiv = document.getElementById(domainId + "-subdomains");
  subdomainsDiv.style.display =
    subdomainsDiv.style.display === "block" ? "none" : "block";
}

function goToApplicationPageNew(domain, subdomain) {
  const url = `application.html?domain=${encodeURIComponent(
    domain
  )}&subdomain=${encodeURIComponent(subdomain)}`;
  window.location.href = url;
}