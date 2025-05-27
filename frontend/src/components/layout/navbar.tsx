import { useState } from "react";
import { Menu } from "lucide-react";
import { cn } from "@/lib/utils";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Link } from "react-router-dom";
import { navItems } from "@/data/navigation-data";
import { Sidebar } from "./sidebar";

const Navbar = () => {
  const [activeItem, setActiveItem] = useState<string>("/");

  return (
    <header className="sticky top-0 z-50 w-full border-b bg-background/95 backdrop-blur supports-[backdrop-filter]:bg-background/60 flex-row justify-center">
      <div className="container mx-auto flex h-16 items-center justify-between">
        <div className="flex items-center gap-6">
          <Link to="/" className="flex items-center">
            <span className="font-bold text-xl">Killinary</span>
          </Link>

          {/* Nav items next to brand in desktop view */}
          <nav className="hidden md:flex gap-6">
            {navItems.map((item) => (
              <Link
                key={item.to}
                to={item.to}
                className={cn(
                  "text-sm font-medium transition-colors hover:text-primary",
                  item.to === activeItem
                    ? "text-foreground"
                    : "text-muted-foreground"
                )}
                onClick={() => setActiveItem(item.to)}
              >
                {item.title}
              </Link>
            ))}
          </nav>
        </div>

        {/* Right side with language selection and mobile menu */}
        <div className="flex items-center gap-2">
          {/* Sign in - only visible on desktop */}
          <div className="hidden md:flex gap-1">
            <Button
              variant={"outline"}
              size={"sm"}
              className="px-2 min-w-[40px]"
            >
              Sign In
            </Button>
            <Button
              variant={"default"}
              size={"sm"}
              className="px-2 min-w-[40px]"
            >
              Sign Up
            </Button>
          </div>

          {/* Mobile menu button on the right */}
          <Sheet>
            <SheetTrigger asChild>
              <Button variant="ghost" size="icon" className="md:hidden ml-2">
                <Menu className="h-5 w-5" />
                <span className="sr-only">Toggle menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent side="right" className="pl-0">
              <SheetTitle className="sr-only">
                Mobile Navigation Menu
              </SheetTitle>
              <SheetDescription className="sr-only">
                Main mobile navigation
              </SheetDescription>
              <Sidebar
                items={navItems}
                activeItem={activeItem}
                setActiveItem={setActiveItem}
              />
            </SheetContent>
          </Sheet>
        </div>
      </div>
    </header>
  );
};

export default Navbar;
