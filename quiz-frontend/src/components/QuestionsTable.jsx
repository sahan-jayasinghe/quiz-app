// ============================================================
// FILE: src/components/QuestionsTable.jsx
// PURPOSE: Fetches questions from the backend and renders them
//          in a styled, searchable, sortable table.
//
// REACT CONCEPTS USED HERE:
//   • useState       — local component state
//   • useEffect      — side effects (data fetching)
//   • useMemo        — derived/computed state
//   • props          — data from parent
//   • conditional rendering
//   • list rendering with .map()
// ============================================================

import { useState, useEffect, useMemo } from "react";
import { getAllQuestions } from "../api/apiService";

// -------------------------------------------------------
// Badge helper — maps a difficultyLevel string to a CSS class.
// This is a plain JavaScript function, not a component.
// -------------------------------------------------------
function getDifficultyClass(level) {
  switch (level?.toLowerCase()) {
    case "easy":   return "badge badge-easy";
    case "medium": return "badge badge-medium";
    case "hard":   return "badge badge-hard";
    default:       return "badge badge-default";
  }
}

// -------------------------------------------------------
// QuestionsTable component
// Props:
//   token      — string — the JWT from the parent (App.jsx)
//   username   — string — logged-in user's name (for greeting)
//   onLogout   — function — called when user clicks "Logout"
// -------------------------------------------------------
function QuestionsTable({ token, username, onLogout }) {

  // ── State ───────────────────────────────────────────────
  // `questions` holds the array of QuestionDto objects from the server.
  // We start with an empty array so the table renders correctly
  // even before the data arrives.
  const [questions, setQuestions] = useState([]);

  // `isLoading` — true while the fetch is in progress.
  const [isLoading, setIsLoading] = useState(false);

  // `error` — holds an error message string if the fetch failed.
  const [error, setError] = useState("");

  // `search` — the text the user has typed in the search box.
  const [search, setSearch] = useState("");

  // `categoryFilter` — the category the user has selected from the dropdown.
  const [categoryFilter, setCategoryFilter] = useState("All");

  // `sortConfig` — which column to sort by and in which direction.
  // { key: "id", direction: "asc" | "desc" }
  const [sortConfig, setSortConfig] = useState({ key: "id", direction: "asc" });

  // ── useEffect ───────────────────────────────────────────
  // REACT CONCEPT — useEffect:
  // useEffect(callback, [dependencies]) runs the callback
  // AFTER the component renders. It is used for "side effects" —
  // things that happen outside of rendering, like:
  //   • Fetching data from a server
  //   • Setting up a timer
  //   • Subscribing to an event
  //
  // The [token] dependency array means: "run this effect whenever
  // `token` changes". When this component first mounts (appears on
  // screen), `token` goes from undefined to its value, so the
  // effect runs and fetches the data immediately on load.
  useEffect(() => {
    // We define and immediately call an async function INSIDE
    // useEffect because the effect callback itself cannot be async.
    async function fetchQuestions() {
      setIsLoading(true);
      setError("");

      try {
        // getAllQuestions() returns the array of question objects.
        const data = await getAllQuestions(token);
        setQuestions(data);
      } catch (err) {
        setError(err.message);
      } finally {
        setIsLoading(false);
      }
    }

    // Only fetch if we have a valid token.
    if (token) {
      fetchQuestions();
    }
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [token]); // ← dependency array

  // ── useMemo — unique categories list ───────────────────
  // REACT CONCEPT — useMemo:
  // useMemo(fn, [deps]) caches the return value of fn.
  // React only re-runs fn when something in [deps] changes.
  // We use this so we don't re-compute the category list
  // on every single keystroke the user types in the search box.
  const categories = useMemo(() => {
    // Set removes duplicates automatically.
    const unique = new Set(questions.map((q) => q.category));
    return ["All", ...Array.from(unique).sort()];
  }, [questions]); // only recompute when `questions` changes

  // ── useMemo — filtered + sorted questions ───────────────
  // This computed value derives a new array from `questions`
  // based on the current search text, category filter, and sort.
  const filteredQuestions = useMemo(() => {
    let result = [...questions]; // copy so we don't mutate state

    // 1. Filter by category
    if (categoryFilter !== "All") {
      result = result.filter((q) => q.category === categoryFilter);
    }

    // 2. Filter by search text
    if (search.trim()) {
      const lower = search.toLowerCase();
      result = result.filter(
        (q) =>
          q.questionTitle?.toLowerCase().includes(lower) ||
          q.category?.toLowerCase().includes(lower) ||
          q.difficultyLevel?.toLowerCase().includes(lower)
      );
    }

    // 3. Sort by the selected column
    result.sort((a, b) => {
      const aVal = a[sortConfig.key] ?? "";
      const bVal = b[sortConfig.key] ?? "";

      if (aVal < bVal) return sortConfig.direction === "asc" ? -1 : 1;
      if (aVal > bVal) return sortConfig.direction === "asc" ? 1 : -1;
      return 0;
    });

    return result;
  }, [questions, search, categoryFilter, sortConfig]);
  // Recompute only when any of these four values change.

  // ── handleSort ─────────────────────────────────────────
  // Called when the user clicks a column header.
  // If the same column is clicked twice, we flip the direction.
  function handleSort(key) {
    setSortConfig((prev) =>
      prev.key === key
        ? { key, direction: prev.direction === "asc" ? "desc" : "asc" }
        : { key, direction: "asc" }
    );
  }

  // Helper: render a sort arrow indicator in the column header.
  function SortArrow({ columnKey }) {
    if (sortConfig.key !== columnKey) return <span className="sort-arrow inactive">↕</span>;
    return (
      <span className="sort-arrow active">
        {sortConfig.direction === "asc" ? "↑" : "↓"}
      </span>
    );
  }

  // ── RENDER ─────────────────────────────────────────────
  return (
    <div className="page-container">

      {/* ── TOP NAV ──────────────────────────────────────── */}
      <header className="top-nav">
        <div className="nav-brand">
          <span className="nav-icon">🎓</span>
          <span className="nav-title">Quiz Admin Panel</span>
        </div>
        <div className="nav-right">
          <span className="nav-user">👤 {username}</span>
          <button className="logout-btn" onClick={onLogout}>
            Logout
          </button>
        </div>
      </header>

      {/* ── STATS CARDS ──────────────────────────────────── */}
      <div className="stats-row">
        <div className="stat-card">
          <div className="stat-number">{questions.length}</div>
          <div className="stat-label">Total Questions</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{filteredQuestions.length}</div>
          <div className="stat-label">Showing</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">{categories.length - 1}</div>
          <div className="stat-label">Categories</div>
        </div>
        <div className="stat-card">
          <div className="stat-number">
            {questions.filter((q) => q.difficultyLevel?.toLowerCase() === "easy").length}
          </div>
          <div className="stat-label">Easy</div>
        </div>
      </div>

      {/* ── CONTROLS ─────────────────────────────────────── */}
      <div className="controls-bar">
        {/* SEARCH BOX */}
        <div className="search-wrapper">
          <span className="search-icon">🔍</span>
          <input
            type="text"
            className="search-input"
            placeholder="Search questions, category, difficulty…"
            value={search}
            // Every keystroke updates `search`, which triggers
            // the useMemo to recompute `filteredQuestions`.
            onChange={(e) => setSearch(e.target.value)}
          />
          {/* Show a clear button only when there is text to clear */}
          {search && (
            <button className="clear-btn" onClick={() => setSearch("")}>
              ✕
            </button>
          )}
        </div>

        {/* CATEGORY DROPDOWN */}
        <select
          className="category-select"
          value={categoryFilter}
          onChange={(e) => setCategoryFilter(e.target.value)}
        >
          {/*
            REACT CONCEPT — List Rendering with .map():
            Whenever you render a list, React needs a unique `key`
            prop on each element. This helps React know which items
            changed, were added, or were removed — making updates
            much faster (this is the "reconciliation" algorithm).
            Never use the array index as a key if the list can reorder.
          */}
          {categories.map((cat) => (
            <option key={cat} value={cat}>
              {cat}
            </option>
          ))}
        </select>
      </div>

      {/* ── CONTENT AREA ─────────────────────────────────── */}

      {/* REACT CONCEPT — Conditional Rendering with ternary:
          We show either a loading spinner, an error message,
          an empty state, or the actual table — never more than one. */}
      {isLoading ? (
        <div className="state-box">
          <div className="spinner" />
          <p>Fetching questions from server…</p>
        </div>
      ) : error ? (
        <div className="state-box error-state">
          <span className="state-icon">❌</span>
          <p className="state-msg">{error}</p>
          <button className="retry-btn" onClick={() => setError("")}>
            Dismiss
          </button>
        </div>
      ) : filteredQuestions.length === 0 ? (
        <div className="state-box">
          <span className="state-icon">🔍</span>
          <p className="state-msg">No questions match your filters.</p>
        </div>
      ) : (
        /* ── THE TABLE ──────────────────────────────────── */
        <div className="table-wrapper">
          <table className="questions-table">
            <thead>
              <tr>
                {/* Each <th> is clickable to sort by that column */}
                <th onClick={() => handleSort("id")} className="th-sortable">
                  # <SortArrow columnKey="id" />
                </th>
                <th onClick={() => handleSort("questionTitle")} className="th-sortable">
                  Question <SortArrow columnKey="questionTitle" />
                </th>
                <th>Option A</th>
                <th>Option B</th>
                <th>Option C</th>
                <th>Option D</th>
                <th onClick={() => handleSort("rightAnswer")} className="th-sortable">
                  Answer <SortArrow columnKey="rightAnswer" />
                </th>
                <th onClick={() => handleSort("difficultyLevel")} className="th-sortable">
                  Difficulty <SortArrow columnKey="difficultyLevel" />
                </th>
                <th onClick={() => handleSort("category")} className="th-sortable">
                  Category <SortArrow columnKey="category" />
                </th>
              </tr>
            </thead>

            <tbody>
              {/*
                REACT CONCEPT — Rendering a list of components:
                .map() iterates over filteredQuestions and returns a
                <tr> element for each question object.
                The `key` prop must be unique — we use q.id from the DB.
              */}
              {filteredQuestions.map((q) => (
                <tr key={q.id} className="table-row">
                  <td className="td-id">{q.id}</td>
                  <td className="td-question">{q.questionTitle}</td>
                  <td>{q.option1}</td>
                  <td>{q.option2}</td>
                  <td>{q.option3}</td>
                  <td>{q.option4}</td>
                  {/* Highlight the correct answer in green */}
                  <td className="td-answer">{q.rightAnswer}</td>
                  <td>
                    {/* Badge component using a className derived from the value */}
                    <span className={getDifficultyClass(q.difficultyLevel)}>
                      {q.difficultyLevel}
                    </span>
                  </td>
                  <td>
                    <span className="badge badge-category">{q.category}</span>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>

          {/* Footer row count */}
          <div className="table-footer">
            Showing {filteredQuestions.length} of {questions.length} questions
          </div>
        </div>
      )}
    </div>
  );
}

export default QuestionsTable;
