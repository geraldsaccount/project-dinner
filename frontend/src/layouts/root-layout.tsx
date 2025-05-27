import { Footer, Navbar } from "@/components";
import type { PropsWithChildren } from "react";
import { Outlet } from "react-router-dom";

const RootLayout: React.FC<PropsWithChildren> = () => {
  return (
    <div className="flex flex-col min-h-screen">
      <Navbar />
      <main className={"container mx-auto flex-1 relative py-4 mt-16 h-full"}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default RootLayout;
