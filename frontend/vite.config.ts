import { defineConfig } from "vite";
import path from "path";
import tailwindcss from "@tailwindcss/vite";
import react from "@vitejs/plugin-react";

// https://vite.dev/config/
export default defineConfig({
  plugins: [react(), tailwindcss()],
  resolve: {
    // Enables non-relative imports from the 'src' folder using '@/'
    alias: {
      "@": path.resolve(__dirname, "./src"),
      "@data": path.resolve(__dirname, "./data"),
      /*
       * only for example purposes - creates an alias "@utils" pointing to "src/utils"
       * "@utils": path.resolve(__dirname, "src/utils"),
       * */
    },
  },
  server: {
    proxy: {
      "/api": {
        target: `http://localhost:8080`,
      },
    },
  },
});
