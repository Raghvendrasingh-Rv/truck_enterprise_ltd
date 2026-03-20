/** @type {import('tailwindcss').Config} */
export default {
  content: ["./index.html", "./src/**/*.{js,jsx}"],
  theme: {
    extend: {
      colors: {
        ink: "#18222f",
        paper: "#f8f6f1",
        accent: "#d97706",
        steel: "#476072",
        pine: "#264653",
        mist: "#e8eef2",
      },
      boxShadow: {
        card: "0 20px 50px rgba(24, 34, 47, 0.08)",
      },
      borderRadius: {
        xl2: "1.25rem",
      },
    },
  },
  plugins: [],
};
