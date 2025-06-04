import { Footer, Navbar } from "@/components";
import type { PropsWithChildren } from "react";
import { Outlet } from "react-router-dom";
import { Toaster } from "sonner";

const RootLayout: React.FC<PropsWithChildren> = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <Toaster position="top-right" />
      <Navbar />
      <main
        className={
          "container mx-auto flex-1 relative py-4 mt-12 h-full md:px-8 px-4"
        }
      >
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default RootLayout;
