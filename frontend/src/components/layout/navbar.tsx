import { useEffect, useState } from "react";
import { Menu } from "lucide-react";
import { Button } from "@/components/ui/button";
import {
  Sheet,
  SheetContent,
  SheetDescription,
  SheetTitle,
  SheetTrigger,
} from "@/components/ui/sheet";
import { Link, useLocation } from "react-router-dom";
import { navItems } from "@/data/navigation-data";
import { Sidebar } from "./sidebar";
import {
  SignedIn,
  SignedOut,
  SignInButton,
  UserButton,
} from "@clerk/clerk-react";
import { cn } from "@/lib/utils";

const Navbar = () => {
  const location = useLocation();
  // Get the first path segment after the domain, or "/" if root
  const getActiveItem = () => {
    const path = location.pathname.split("/").filter(Boolean)[0];
    return path ? `/${path}` : "/";
  };
  const [activeItem, setActiveItem] = useState<string>(getActiveItem());

  // Update activeItem when the route changes
  useEffect(() => {
    setActiveItem(getActiveItem());
  }, [location.pathname]);

  return (
    <header className="flex sticky top-0 z-50 w-full border-b-2 border-foreground bg-background flex-row justify-center">
      {" "}
      {/* Brutalist: Solid background, thick foreground border */}
      <div className="container mx-auto flex h-16 items-center justify-between md:px-8 px-4">
        <div className="flex items-center gap-6">
          <Link to="/" className="flex items-center">
            <span className="font-bold text-xl uppercase text-foreground">
              Killinary
            </span>{" "}
            {/* Brutalist: Uppercase, strong foreground color */}
          </Link>

          <nav className="hidden md:flex gap-6">
            {navItems.map((item) => (
              <Link
                key={item.to}
                to={item.to}
                className={cn(
                  "text-sm font-bold uppercase transition-all duration-0 hover:bg-foreground hover:text-background text-foreground px-2 py-1" /* Brutalist: Bold, uppercase, stark hover, no transition duration */,
                  item.to === activeItem ? "bg-foreground text-background" : ""
                )}
                onClick={() => setActiveItem(item.to)}
              >
                {item.title}
              </Link>
            ))}
          </nav>
        </div>

        <div className="flex items-center gap-2">
          <SignedOut>
            <div className="hidden md:flex">
              <SignInButton>
                <Button
                  variant={"default"}
                  size={"sm"}
                  className="px-4 py-2 bg-foreground text-background border-2 border-foreground hover:bg-background hover:text-foreground duration-0 rounded-none" /* Brutalist: High contrast, sharp edges, no min-width, no transition duration */
                >
                  Sign In
                </Button>
              </SignInButton>
            </div>
          </SignedOut>
          <SignedIn>
            <UserButton
              appearance={{
                elements: {
                  userButtonAvatarBox:
                    "rounded-none border-2 border-foreground",
                },
              }}
            />{" "}
            {/* Brutalist: Remove border-radius on avatar if possible through Clerk */}
          </SignedIn>

          <Sheet>
            <SheetTrigger asChild>
              <Button
                variant="ghost"
                size="icon"
                className="md:hidden ml-2 border-2 border-foreground bg-foreground text-background hover:bg-background hover:text-foreground duration-0 rounded-none"
              >
                {" "}
                {/* Brutalist: High contrast, sharp edges for mobile trigger, no transition duration */}
                <Menu className="h-5 w-5" />
                <span className="sr-only">Toggle menu</span>
              </Button>
            </SheetTrigger>
            <SheetContent
              side="right"
              className="p-0 border-l-2 border-foreground bg-background rounded-none"
            >
              {" "}
              {/* Brutalist: No padding, sharp left border, no rounded corners */}
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
