// ============================================================
// FILE: src/components/LoginPage.jsx
// PURPOSE: The login form UI component.
//
// REACT CONCEPT — Components:
// A component is a JavaScript function that returns JSX (HTML-like
// syntax). React uses these as building blocks for the UI.
// Each component manages its own piece of the screen.
//
// REACT CONCEPT — Props:
// Props (short for "properties") are how a parent component
// passes data DOWN to a child component. They are read-only —
// the child component cannot modify them directly.
// ============================================================

// `useState` is a React hook for managing local state inside a component.
import { useState } from "react";

// -------------------------------------------------------
// LoginPage component
// Props:
//   onLogin   — function(username, password) — called when user submits
//   error     — string — error message from the parent (from useAuth)
//   isLoading — boolean — whether the login request is in progress
// -------------------------------------------------------
function LoginPage({ onLogin, error, isLoading }) {
  // These two pieces of state track what the user has typed
  // in the username and password input fields.
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  // ── handleSubmit ───────────────────────────────────────
  // This function is called when the user clicks "Login"
  // or presses Enter inside the form.
  function handleSubmit(event) {
    // REACT CONCEPT — Synthetic Events:
    // React wraps browser events in its own "SyntheticEvent"
    // object so they work the same in every browser.
    // event.preventDefault() stops the form from doing its
    // default behavior of reloading the whole page.
    event.preventDefault();

    // Guard: do not submit if fields are empty.
    if (!username.trim() || !password.trim()) return;

    // Call the onLogin prop (which is handleLogin from useAuth).
    // This triggers the actual HTTP request.
    onLogin(username, password);
  }

  // ── JSX ────────────────────────────────────────────────
  // REACT CONCEPT — JSX:
  // JSX looks like HTML but it is actually JavaScript.
  // The React toolchain (Babel/Vite) converts JSX into
  // React.createElement() calls at build time.
  //
  // Key JSX rules:
  //   • className instead of class  (class is a JS reserved word)
  //   • htmlFor instead of for      (for is a JS reserved word)
  //   • JavaScript expressions go inside { curly braces }
  //   • Every component must return a single root element
  return (
    // The outer <div> is the single root element.
    <div className="login-container">
      <div className="login-card">
        {/* Decorative top bar */}
        <div className="login-header">
          <div className="login-icon">🎓</div>
          <h1 className="login-title">Quiz Admin</h1>
          <p className="login-subtitle">Sign in to manage questions</p>
        </div>

        {/* ── FORM ──────────────────────────────────────── */}
        {/* onSubmit — React attaches this as the form's submit handler */}
        <form className="login-form" onSubmit={handleSubmit}>

          {/* USERNAME FIELD */}
          <div className="form-group">
            <label htmlFor="username" className="form-label">
              Username
            </label>
            <input
              id="username"
              type="text"
              className="form-input"
              placeholder="Enter your username"
              value={username}
              // onChange fires on every keystroke.
              // event.target.value is the current text in the input.
              // We update state, which causes React to re-render
              // and keep the input in sync with our state.
              // This pattern is called a "controlled component".
              onChange={(event) => setUsername(event.target.value)}
              // disabled prevents the user from typing while loading.
              disabled={isLoading}
              autoComplete="username"
            />
          </div>

          {/* PASSWORD FIELD */}
          <div className="form-group">
            <label htmlFor="password" className="form-label">
              Password
            </label>
            <input
              id="password"
              type="password"
              className="form-input"
              placeholder="Enter your password"
              value={password}
              onChange={(event) => setPassword(event.target.value)}
              disabled={isLoading}
              autoComplete="current-password"
            />
          </div>

          {/* REACT CONCEPT — Conditional Rendering:
              The && operator renders the element only when the
              left side is truthy. If `error` is an empty string
              (falsy), nothing is rendered. */}
          {error && (
            <div className="error-banner" role="alert">
              <span className="error-icon">⚠️</span>
              {error}
            </div>
          )}

          {/* SUBMIT BUTTON */}
          <button
            type="submit"
            className={`login-btn ${isLoading ? "loading" : ""}`}
            // Disabled while loading to prevent double-submits.
            disabled={isLoading}
          >
            {/* Conditional rendering using a ternary expression:
                condition ? valueIfTrue : valueIfFalse */}
            {isLoading ? (
              <span className="spinner-inline">Signing in…</span>
            ) : (
              "Sign In"
            )}
          </button>
        </form>

        <p className="login-footer">
          Requires ADMIN or TEACHER role
        </p>
      </div>
    </div>
  );
}

// REACT CONCEPT — Default Exports:
// export default makes this the component that other files get
// when they write: import LoginPage from './components/LoginPage'
export default LoginPage;
