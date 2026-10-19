// ============================
// MET VALUES
// ============================
const metValues = {
    running: 8.3,
    cycling: 6.8,
    swimming: 7.0,
    walking: 3.8,
    'jumping-jacks': 10.0
};

// ============================
// API SERVICE
// ============================
const API_URL = "http://localhost:8081/api/exercises";

class ExerciseService {

    async getExercises() {
        const res = await fetch(API_URL);
        if (!res.ok) throw new Error("Failed to load exercises");
        return res.json();
    }

    async addExercise(exercise) {
        const res = await fetch(API_URL, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(exercise)
        });
        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Failed to add exercise");
        }
        return res.json();
    }

    async deleteExercise(id) {
        const res = await fetch(`${API_URL}/${id}`, { method: "DELETE" });
        if (!res.ok) throw new Error("Failed to delete exercise");
    }

    async updateExercise(id, exercise) {
        const res = await fetch(`${API_URL}/${id}`, {
            method: "PUT",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(exercise)
        });
        if (!res.ok) {
            const msg = await res.text();
            throw new Error(msg || "Failed to update exercise");
        }
        return res.json();
    }
}

const exerciseService = new ExerciseService();

// ============================
// GLOBAL STATE
// ============================
let exercises = [];
let calorieWorkouts = [];

// ============================
// CALORIE CALCULATOR
// ============================
document.getElementById('calorie-form').addEventListener('submit', function(e) {
    e.preventDefault();

    const exerciseType = document.getElementById('exercise-type').value;
    const duration = parseFloat(document.getElementById('duration').value);
    const weight = parseFloat(document.getElementById('weight').value);

    const met = metValues[exerciseType];
    const calories = Math.round(met * weight * duration / 60);

    calorieWorkouts.push({
        exercise: exerciseType,
        duration,
        weight,
        calories
    });

    renderCalorieWorkouts();
    this.reset();
});

document.getElementById('reset-calculator').addEventListener('click', function() {
    calorieWorkouts = [];
    renderCalorieWorkouts();
});

function renderCalorieWorkouts() {
    const list = document.getElementById('calorie-list');
    list.innerHTML = '';

    let total = 0;

    calorieWorkouts.forEach((w, i) => {
        total += w.calories;

        const div = document.createElement('div');
        div.style.padding = "10px";
        div.style.marginBottom = "10px";
        div.style.background = "#f0f0f0";
        div.style.borderRadius = "6px";

        div.innerHTML = `
            ${w.exercise} - ${w.duration} min (${w.weight}kg)
            = ${w.calories} calories
            <button onclick="removeCalorieWorkout(${i})">Remove</button>
        `;

        list.appendChild(div);
    });

    document.getElementById('total-calorie-result').textContent = total;
}

function removeCalorieWorkout(index) {
    calorieWorkouts.splice(index, 1);
    renderCalorieWorkouts();
}

// ============================
// EXERCISE LIST (CRUD)
// ============================
async function renderExercises() {
    try {
        exercises = await exerciseService.getExercises();
    } catch (err) {
        showError('exercise-error', 'Could not reach the server. Make sure the backend is running on port 8081.');
        return;
    }

    const list = document.getElementById('exercise-list');
    list.innerHTML = '';

    if (exercises.length === 0) {
        list.innerHTML = '<li style="color:#888;list-style:none;">No exercises logged yet.</li>';
        return;
    }

    exercises.forEach(ex => {
        const li = document.createElement('li');
        li.id = `ex-${ex.id}`;

        li.innerHTML = `
            <span class="ex-display">
                <strong>${ex.name}</strong> — ${ex.duration} min on ${ex.date}
                &nbsp;<span style="color:#888;font-size:.9em;">(~${ex.calories} kcal)</span>
            </span>
            <span class="ex-edit-form" style="display:none;">
                <select class="edit-name">
                    ${['running','cycling','swimming','walking','jumping-jacks'].map(opt =>
                        `<option value="${opt}" ${ex.name === opt ? 'selected' : ''}>${opt}</option>`
                    ).join('')}
                </select>
                <input class="edit-duration" type="number" value="${ex.duration}" min="1" />
                <input class="edit-date" type="date" value="${ex.date}" />
            </span>
            <button class="btn-edit" onclick="startEdit(${ex.id})">Edit</button>
            <button class="btn-save" onclick="saveEdit(${ex.id})" style="display:none;">Save</button>
            <button class="btn-cancel" onclick="cancelEdit(${ex.id})" style="display:none;">Cancel</button>
            <button class="btn-delete" onclick="deleteExercise(${ex.id})">Delete</button>
        `;

        list.appendChild(li);
    });
}

function startEdit(id) {
    const li = document.getElementById(`ex-${id}`);
    li.querySelector('.ex-display').style.display = 'none';
    li.querySelector('.ex-edit-form').style.display = 'inline';
    li.querySelector('.btn-edit').style.display = 'none';
    li.querySelector('.btn-save').style.display = 'inline';
    li.querySelector('.btn-cancel').style.display = 'inline';
    li.querySelector('.btn-delete').style.display = 'none';
}

function cancelEdit(id) {
    const li = document.getElementById(`ex-${id}`);
    li.querySelector('.ex-display').style.display = 'inline';
    li.querySelector('.ex-edit-form').style.display = 'none';
    li.querySelector('.btn-edit').style.display = 'inline';
    li.querySelector('.btn-save').style.display = 'none';
    li.querySelector('.btn-cancel').style.display = 'none';
    li.querySelector('.btn-delete').style.display = 'inline';
}

async function saveEdit(id) {
    const li = document.getElementById(`ex-${id}`);
    const name = li.querySelector('.edit-name').value;
    const duration = parseInt(li.querySelector('.edit-duration').value);
    const date = li.querySelector('.edit-date').value;

    if (!name || !duration || !date) return;

    try {
        await exerciseService.updateExercise(id, { name, duration, date });
        renderExercises();
    } catch (err) {
        showError('exercise-error', err.message);
    }
}

// ============================
// ADD EXERCISE
// ============================
document.getElementById('exercise-form').addEventListener('submit', async function(e) {
    e.preventDefault();
    clearError('exercise-error');

    const name = document.getElementById('exercise-name').value;
    const duration = parseInt(document.getElementById('exercise-duration').value);
    const date = document.getElementById('exercise-date').value;

    if (!name) { showError('exercise-error', 'Please select an exercise.'); return; }
    if (!duration || duration < 1) { showError('exercise-error', 'Duration must be at least 1 minute.'); return; }
    if (!date) { showError('exercise-error', 'Please select a date.'); return; }

    try {
        await exerciseService.addExercise({ name, duration, date });
        this.reset();
        renderExercises();
    } catch (err) {
        showError('exercise-error', err.message);
    }
});

// ============================
// DELETE EXERCISE
// ============================
async function deleteExercise(id) {
    try {
        await exerciseService.deleteExercise(id);
        renderExercises();
    } catch (err) {
        showError('exercise-error', err.message);
    }
}

// ============================
// ERROR HELPERS
// ============================
function showError(elementId, message) {
    let el = document.getElementById(elementId);
    if (!el) {
        el = document.createElement('p');
        el.id = elementId;
        el.style.cssText = 'color:red;font-size:.875rem;margin-top:.5rem;';
        document.getElementById('exercise-tracker').appendChild(el);
    }
    el.textContent = message;
}

function clearError(elementId) {
    const el = document.getElementById(elementId);
    if (el) el.textContent = '';
}

// ============================
// INIT
// ============================
(async function() {
    await renderExercises();
})();
