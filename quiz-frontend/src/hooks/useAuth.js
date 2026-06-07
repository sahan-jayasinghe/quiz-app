// ============================================================
// FILE: src/hooks/useAuth.js
// PURPOSE: A CUSTOM HOOK that manages authentication state.
//
// REACT CONCEPT — Custom Hooks:
// A custom hook is just a JavaScript function whose name starts
// with "use" and that calls built-in React hooks inside it.
// Custom hooks let you extract and reuse stateful logic across
// multiple components WITHOUT duplicating code.
// ============================================================

// We import the built-in React hooks we need.
import { useState, useCallback } from "react";

// We import the login() function from our API service.
import { login } from "../api/apiService";

// -------------------------------------------------------
// useAuth — custom hook
// Returns: { token, username, error, isLoading, handleLogin, handleLogout }
// -------------------------------------------------------
export function useAuth() {
  // ── useState ──────────────────────────────────────────
  // REACT CONCEPT — useState:
  // useState(initialValue) returns an array of exactly 2 things:
  //   [currentValue, setterFunction]
  // Whenever the setter is called, React re-renders the component
  // that uses this hook so the UI reflects the new value.

  // `token` stores the JWT string. null means "not logged in".
  const [token, setToken] = useState(null);

  // `username` stores who is logged in (just for display).
  const [username, setUsername] = useState("");

  // `error` stores any error message from a failed login attempt.
  const [error, setError] = useState("");

  // `isLoading` is true while we are waiting for the server to respond.
  // We use it to disable the login button and show a spinner.
  const [isLoading, setIsLoading] = useState(false);

  // ── useCallback ───────────────────────────────────────
  // REACT CONCEPT — useCallback:
  // useCallback(fn, [deps]) memoizes (caches) a function.
  // It returns the SAME function reference on every render
  // unless something in the deps array changes.
  // This prevents unnecessary re-renders in child components
  // that receive this function as a prop.
  // The empty [] means this function is created only once.

  const handleLogin = useCallback(async (usernameInput, passwordInput) => {
    // Reset error and show spinner
    setError("");
    setIsLoading(true);

    try {
      // Call the API. Because login() is async, we await it.
      // If the server returns an error, login() will throw,
      // and execution jumps to the catch block.
      const jwt = await login(usernameInput, passwordInput);

      // If we reach here, login succeeded. Store the token and username.
      setToken(jwt);
      setUsername(usernameInput);
    } catch (err) {
      // err.message is the string we threw in apiService.js
      setError(err.message);
    } finally {
      // `finally` always runs, whether we succeeded or failed.
      // We always want to hide the spinner when done.
      setIsLoading(false);
    }
  }, []);

  const handleLogout = useCallback(() => {
    // Clear all auth state. React will re-render and show the login form.
    setToken(null);
    setUsername("");
    setError("");
  }, []);

  // Return everything the components need as a plain object.
  return { token, username, error, isLoading, handleLogin, handleLogout };
}
