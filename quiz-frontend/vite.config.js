// ============================================================
// FILE: vite.config.js
// PURPOSE: Configuration for Vite — the build tool and dev server.
//
// WHY VITE?
// Vite is a modern build tool that:
//   • Starts the dev server instantly (no bundling on startup)
//   • Gives you Hot Module Replacement (HMR) — the page updates
//     instantly when you save a file, without a full refresh
//   • Bundles optimally for production using Rollup under the hood
// ============================================================

import { defineConfig } from "vite";

// @vitejs/plugin-react adds JSX support and React Fast Refresh
// (the mechanism that makes HMR work correctly with React hooks).
import react from "@vitejs/plugin-react";

export default defineConfig({
  // The `plugins` array registers Vite plugins.
  // react() here enables:
  //   • Transformation of .jsx files (JSX → JavaScript)
  //   • Automatic JSX runtime (you do NOT need to import React
  //     in every file just to use JSX — Vite handles it)
  //   • React Fast Refresh for instant, state-preserving HMR
  plugins: [react()],

  server: {
    // Force IPv4 binding — on Windows, Node sometimes tries ::
    // (IPv6 loopback) first, which can cause permission errors.
    host: "127.0.0.1",
    port: 3050,

    // ── CORS PROXY ──────────────────────────────────────────
    // PROBLEM: The browser's "Same-Origin Policy" blocks requests
    // from http://localhost:5173 (React) to http://localhost:8080
    // (Spring Boot) because they are on different ports = different
    // origins. This is called a CORS error.
    //
    // SOLUTION A (used here): Vite's built-in proxy.
    // Any request from React to "/api/..." is silently rewritten
    // by Vite's dev server to "http://localhost:8080/...".
    // Since the request now appears to come from the SAME origin as
    // Spring Boot, there is no CORS issue at all.
    //
    // SOLUTION B: Add @CrossOrigin on your Spring Boot controllers.
    // We handle BOTH here so it works in every scenario.
    proxy: {
      // Any fetch() call starting with "/api" in our React code...
      "/api": {
        target: "http://127.0.0.1:8080", // ...gets forwarded here
        changeOrigin: true,             // rewrite the Host header
        // Strip "/api" prefix before forwarding:
        // fetch("/api/auth/login") → http://localhost:8080/auth/login
        rewrite: (path) => path.replace(/^\/api/, ""),
      },
    },
  },
});
