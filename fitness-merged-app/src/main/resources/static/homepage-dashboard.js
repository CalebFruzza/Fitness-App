const dashboardApi = {
    async getSummary(range) {
        const response = await fetch(`/api/dashboard/summary?range=${encodeURIComponent(range)}`);
        if (!response.ok) {
            throw new Error("Unable to load dashboard summary.");
        }
        return response.json();
    }
};

function updateHomepageDashboard(summary) {
    document.getElementById("calories-left").textContent = `${summary.caloriesLeft} kcal`;
    document.getElementById("net-calories").textContent = `${summary.netCalories} kcal`;
    document.getElementById("calories-in").textContent = summary.caloriesIn;
    document.getElementById("calories-out").textContent = summary.caloriesOut;
    document.getElementById("net-calories-ring").textContent = summary.netCalories;

    const fill = document.getElementById("stats-fill");
    if (fill) {
        fill.style.width = `${summary.progressPercent}%`;
    }

    const circles = document.querySelectorAll(".progress-rings circle");
    const progressValues = [
        summary.goal > 0 ? summary.caloriesIn / summary.goal : 0,
        summary.caloriesOut / 600,
        summary.goal > 0 ? summary.netCalories / summary.goal : 0
    ];

    circles.forEach((circle, index) => {
        const circumference = 314;
        circle.style.strokeDasharray = circumference;
        const offset = circumference - Math.min(Math.max(progressValues[index], 0), 1) * circumference;
        circle.style.strokeDashoffset = offset;
    });
}

window.addEventListener("DOMContentLoaded", () => {
    const dateRangeSelect = document.getElementById("date-range-select");
    const dashboardDateEl = document.getElementById("dashboard-date");

    async function loadRange(range) {
        try {
            const summary = await dashboardApi.getSummary(range);
            dashboardDateEl.textContent = summary.label;
            updateHomepageDashboard(summary);
        } catch (error) {
            console.error(error);
        }
    }

    if (dateRangeSelect) {
        dateRangeSelect.addEventListener("change", () => {
            loadRange(dateRangeSelect.value);
        });
    }

    setTimeout(() => {
        loadRange(dateRangeSelect ? dateRangeSelect.value || "today" : "today");
    }, 0);
});
