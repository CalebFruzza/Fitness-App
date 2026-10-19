
document.addEventListener("DOMContentLoaded", () => {

  let userProfile = null; // Holds age, sex, height, weight, activity and goal
  let dailyLogs = {};     // Holds calories per date: { "YYYY-MM-DD": { breakfast, lunch, dinner, snacks, total } }


 
  // --- Profile inputs ---
  const ageInput = document.getElementById("age");
  const sexSelect = document.getElementById("sex");
  const heightInput = document.getElementById("height");
  const weightInput = document.getElementById("weight");
  const activitySelect = document.getElementById("activity");
  const goalSelect = document.getElementById("goal");
  const saveProfileBtn = document.getElementById("saveProfileBtn");
  const profileMsg = document.getElementById("profileMsg");

  // --- Calculator / results ---
  const calcBtn = document.getElementById("calcBtn");
  const calcError = document.getElementById("calcError");
  const resultsBox = document.getElementById("results");
  const maintenanceSpan = document.getElementById("maintenanceKcal");
  const targetSpan = document.getElementById("targetKcal");

  // --- Macro sliders ---
  const proteinPctSlider = document.getElementById("proteinPct");
  const carbPctSlider = document.getElementById("carbPct");
  const fatPctSlider = document.getElementById("fatPct");

  const proteinPctLabel = document.getElementById("proteinPctLabel");
  const carbPctLabel = document.getElementById("carbPctLabel");
  const fatPctLabel = document.getElementById("fatPctLabel");
  const macroTotalPctSpan = document.getElementById("macroTotalPct");

  const proteinGramsSpan = document.getElementById("proteinGrams");
  const carbGramsSpan = document.getElementById("carbGrams");
  const fatGramsSpan = document.getElementById("fatGrams");

  // --- Tracker inputs ---
  const trackDateInput = document.getElementById("trackDate");
  const calBreakfast = document.getElementById("calBreakfast");
  const calLunch = document.getElementById("calLunch");
  const calDinner = document.getElementById("calDinner");
  const calSnacks = document.getElementById("calSnacks");
  const updateTrackerBtn = document.getElementById("updateTrackerBtn");
  const trackerMsg = document.getElementById("trackerMsg");

  // --- Dashboard visuals ---
  const dashTarget = document.getElementById("dashTarget");
  const dashTotal = document.getElementById("dashTotal");
  const dashRemaining = document.getElementById("dashRemaining");
  const progressFill = document.getElementById("progressFill");

  // --- Circular gauge ---
  const progressCircle = document.getElementById("progressCircle");
  const circlePercent = document.getElementById("circlePercent");


  /**
   * Attaches validation to an input so:
   * - Only digits are allowed (no letters/symbols)
   * - Value is clamped between a minimum and maximum
   */
  function enforceNumericRange(input, min, max) {
    if (!input) return; // Safety check if element is missing

    input.addEventListener("input", () => {
      // Remove any non-digit character as the user types
      input.value = input.value.replace(/[^0-9]/g, "");

      // Empty is allowed while typing; final checks happen on submit
      if (input.value === "") return;

      let val = Number(input.value);

      // Clamp to sensible limits
      if (val < min) val = min;
      if (val > max) val = max;

      input.value = val;
    });
  }

  // Apply validation to profile fields (realistic human ranges)
  enforceNumericRange(ageInput, 10, 100);        // Ages 10–100
  enforceNumericRange(heightInput, 120, 220);    // Heights 120–220 cm
  enforceNumericRange(weightInput, 35, 250);     // Weights 35–250 kg

  // Apply validation to meal inputs (reasonable per-meal calories)
  enforceNumericRange(calBreakfast, 0, 2500);    // Breakfast 0–2500 kcal
  enforceNumericRange(calLunch, 0, 2500);        // Lunch 0–2500 kcal
  enforceNumericRange(calDinner, 0, 2500);       // Dinner 0–2500 kcal
  enforceNumericRange(calSnacks, 0, 2500);       // Snacks 0–2500 kcal


  // ============================================================
  // 4. INITIALISE CIRCLE GAUGE
  // ============================================================

  let circleCircumference = 0; // Used for stroke offset animation

  if (progressCircle) {
    const radius = progressCircle.r.baseVal.value;         // Get radius from SVG
    circleCircumference = 2 * Math.PI * radius;            // Circumference calc

    progressCircle.style.strokeDasharray = `${circleCircumference} ${circleCircumference}`;
    // Above draws a full ring

    progressCircle.style.strokeDashoffset = circleCircumference;
    // Above hides the ring → 0% progress visual
  }


 

  // Read target kcal from UI text and return as number (or null if invalid)
  function getTargetKcal() {
    const n = Number(targetSpan.textContent);
    return Number.isFinite(n) ? n : null;
  }

  // Set the date picker to today's date when page loads
  function initTodayDate() {
    const today = new Date();
    trackDateInput.value = today.toISOString().substring(0, 10);
  }

  // Update bar, circle gauge and remaining calories
  function updateProgressUI(total, target) {
    total = Number(total) || 0; // Always treat as a number

    // If we don't yet have a target, reset the visuals
    if (!target || !Number.isFinite(target) || target <= 0) {
      dashTarget.textContent = "–";
      dashTotal.textContent = total.toString();
      dashRemaining.textContent = "–";
      progressFill.style.width = "0%";
      circlePercent.textContent = "0%";
      if (progressCircle) {
        progressCircle.style.strokeDashoffset = circleCircumference;
      }
      return;
    }

    // Show numbers underneath dashboard
    dashTarget.textContent = target.toString();
    dashTotal.textContent = total.toString();
    dashRemaining.textContent = (target - total).toString();

    // Calculate percentage of goal reached
    let percent = (total / target) * 100;
    if (!Number.isFinite(percent)) percent = 0;

    // Progress bar can go slightly over 100% but is capped visually
    const barPercent = Math.min(percent, 160);

    // Circle gauge is clamped at 100% so it doesn't look punitive
    const circlePercentValue = Math.max(0, Math.min(percent, 100));

    // Apply bar width
    progressFill.style.width = barPercent + "%";

    // Update text inside circle
    circlePercent.textContent = Math.round(circlePercentValue) + "%";

    // Animate SVG stroke based on percentage
    if (progressCircle) {
      const offset = circleCircumference * (1 - circlePercentValue / 100);
      progressCircle.style.strokeDashoffset = offset;
    }
  }

  // Convert macro slider percentages into gram values and update labels
  function updateMacroUI() {
    const target = getTargetKcal();
    if (!target) return; // No target → nothing to convert

    // Read slider values (percentages)
    const p = Number(proteinPctSlider.value);
    const c = Number(carbPctSlider.value);
    const f = Number(fatPctSlider.value);

    // Update text labels for sliders
    proteinPctLabel.textContent = `${p}%`;
    carbPctLabel.textContent = `${c}%`;
    fatPctLabel.textContent = `${f}%`;

    // Show total of all macros
    macroTotalPctSpan.textContent = `${p + c + f}%`;

    // Convert kcal to grams using correct energy values:
    // Protein: 4 kcal/g, Carbs: 4 kcal/g, Fats: 9 kcal/g
    proteinGramsSpan.textContent = Math.round((target * p) / 100 / 4) + "g";
    carbGramsSpan.textContent    = Math.round((target * c) / 100 / 4) + "g";
    fatGramsSpan.textContent     = Math.round((target * f) / 100 / 9) + "g";
  }

  // Load a log from RAM for a selected date and update UI
  function loadLogForDate(dateStr) {
    const log = dailyLogs[dateStr];

    if (!log) {
      // No log → set all inputs to empty and reset progress bar
      calBreakfast.value = "";
      calLunch.value = "";
      calDinner.value = "";
      calSnacks.value = "";
      updateProgressUI(0, getTargetKcal());
      trackerMsg.textContent = "";
      return;
    }

    // Fill fields from stored log
    calBreakfast.value = log.breakfast;
    calLunch.value = log.lunch;
    calDinner.value = log.dinner;
    calSnacks.value = log.snacks;

    // Update dashboard with stored total
    updateProgressUI(log.total, getTargetKcal());
    trackerMsg.textContent = `Loaded tracker for ${dateStr}.`;
  }


  

  initTodayDate();                       // Set today's date initially
  loadLogForDate(trackDateInput.value);  // Load any in-memory 



  saveProfileBtn.addEventListener("click", () => {

    // Build profile object
    const profile = {
      age: Number(ageInput.value),
      sex: sexSelect.value,
      height: Number(heightInput.value),
      weight: Number(weightInput.value),
      activity: Number(activitySelect.value),
      goal: goalSelect.value,
    };

    // Check presence of all fields
    if (!profile.age || !profile.sex || !profile.height || !profile.weight || !profile.activity || !profile.goal) {
      profileMsg.textContent = "Please complete all profile fields.";
      return;
    }

    // Extra range validation 
    if (profile.age < 10 || profile.age > 100) {
      profileMsg.textContent = "Age must be between 10 and 100.";
      return;
    }
    if (profile.height < 120 || profile.height > 220) {
      profileMsg.textContent = "Height must be between 120 and 220 cm.";
      return;
    }
    if (profile.weight < 35 || profile.weight > 250) {
      profileMsg.textContent = "Weight must be between 35 and 250 kg.";
      return;
    }
    fetch("/api/profiles", {
  method: "POST",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify(profile)
  })
  .then(res => {
  if (!res.ok) throw new Error("Failed to save");
  return res.json();
  })
  .then(data => {
  userProfile = data; // store response from backend
  profileMsg.textContent = "Profile saved ";
  })
  .catch(err => {
  profileMsg.textContent = "Error saving profile ";
  console.error(err);
  });

    
  });




  calcBtn.addEventListener("click", () => {

    // Ensure we actually have a profile stored
    if (!userProfile) {
      calcError.textContent = "Please save your profile before calculating.";
      resultsBox.style.display = "none";
      return;
    }

    const age = userProfile.age;
    const sex = userProfile.sex;
    const height = userProfile.heightCm;
    const weight = userProfile.weightKg;
    const activity = userProfile.activityLevel;
    const goal = userProfile.goal;

    // Safety check again in case profile corrupted
    if (!age || !sex || !height || !weight || !activity) {
      calcError.textContent = "Profile is incomplete.";
      resultsBox.style.display = "none";
      return;
    }

    calcError.textContent = ""; // Clear any old error message

    // Calculate BMR using Mifflin–St Jeor
    let bmr;
    if (sex === "female") {
      bmr = 10 * weight + 6.25 * height - 5 * age - 161;
    } else {
      bmr = 10 * weight + 6.25 * height - 5 * age + 5;
    }

    // Apply activity multiplier
    const maintenance = Math.round(bmr * activity);

    // Adjust target based on goal
    let target = maintenance;
    if (goal === "mild-loss") target -= 250;
    if (goal === "mild-gain") target += 250;

    // Display results
    maintenanceSpan.textContent = maintenance.toString();
    targetSpan.textContent = target.toString();
  
    
    const profileId = userProfile?.id;

    if (!profileId) {
      calcError.textContent = "Save profile first.";
      return;
    }
    
    fetch(`/api/plans/calculate/${profileId}`, {
      method: "POST"
    })
    .then(res => {
      if (!res.ok) throw new Error("Failed to calculate plan");
      return res.json();
    })
    .then(data => {
      console.log("Plan from backend:", data);
      maintenanceSpan.textContent = data.maintenanceCalories;
      targetSpan.textContent = data.targetCalories;
      
    
      updateMacroUI(); // ✅ updates grams + percentages
      resultsBox.style.display = "block"; // ✅ shows results section
    })
  
    .catch(err => {
      calcError.textContent = "Error calculating plan ❌";
      console.error(err);
    });
  });



  proteinPctSlider.addEventListener("input", updateMacroUI);
  carbPctSlider.addEventListener("input", updateMacroUI);
  fatPctSlider.addEventListener("input", updateMacroUI);




  trackDateInput.addEventListener("change", () => {
    loadLogForDate(trackDateInput.value);
  });



  updateTrackerBtn.addEventListener("click", () => {

    const dateStr = trackDateInput.value;
    if (!dateStr) {
      trackerMsg.textContent = "Please choose a date first.";
      return;
    }

    // Convert fields to numbers (empty string becomes 0)
    const b = Number(calBreakfast.value) || 0;
    const l = Number(calLunch.value) || 0;
    const d = Number(calDinner.value) || 0;
    const s = Number(calSnacks.value) || 0;
    const total = b + l + d + s;

    // Additional safety checks: avoid unrealistic daily totals
    if (b > 2500 || l > 2500 || d > 2500 || s > 2500) {
      trackerMsg.textContent = "Single meals should not exceed 2500 kcal.";
      return;
    }

    if (total > 6000) {
      trackerMsg.textContent = "Daily calories above 6000 may not be realistic for most users.";
      return;
    }

    // Store log only in memory for this session
    const profileId = userProfile?.id; // make sure profile is saved first

    if (!profileId) {
     trackerMsg.textContent = "Please save profile first.";
     return;
    }

    const logData = {
     date: dateStr,
     breakfast: b,
     lunch: l,
     dinner: d,
     snacks: s,
     total: total
};

fetch(`/api/logs/${profileId}`, {
  method: "POST",
  headers: {
    "Content-Type": "application/json"
  },
  body: JSON.stringify(logData)
})
.then(res => {
  if (!res.ok) throw new Error("Failed to save log");
  return res.json();
})
.then(data => {
  trackerMsg.textContent = "Saved ";
  updateProgressUI(total, getTargetKcal());
})
.catch(err => {
  trackerMsg.textContent = "Error saving log ";
  console.error(err);
});


  });

});


