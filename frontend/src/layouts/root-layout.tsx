import Footer from "@/components/layout/footer";
import Navbar from "@/components/layout/navbar";
import { PropsWithChildren } from "react";
import { Outlet } from "react-router-dom";
import { Toaster } from "sonner";

const RootLayout: React.FC<PropsWithChildren> = () => {
  return (
    <div className="flex flex-col min-h-screen items-center">
      <Toaster position="top-right" />
      <Navbar />
      <main className={"container flex-1 relative py-4  h-full md:px-8 px-4"}>
        <Outlet />
      </main>
      <Footer />
    </div>
  );
};

export default RootLayout;
