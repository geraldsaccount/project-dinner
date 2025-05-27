import { cn } from "@/lib/utils";
import type { MobileNavProps } from "@/types";
import { Separator } from "@radix-ui/react-separator";
import { Link } from "react-router-dom";
import { Button } from "../ui/button";
import { footerItems } from "@/data/navigation-data";
import { SignedOut, SignInButton } from "@clerk/clerk-react";

export function Sidebar({ items, activeItem, setActiveItem }: MobileNavProps) {
  return (
    <div className="flex flex-col justify-between h-full py-4">
      <div className="px-7">
        <div className="flex justify-end">
          <Link to="/" className="flex items-center">
            <span className="font-bold text-xl">Killinary</span>
          </Link>
        </div>

        <div className="flex flex-col space-y-3 mt-6">
          {items.map((item) => (
            <Link
              key={item.to}
              to={item.to}
              className={cn(
                "text-base font-medium transition-colors hover:text-primary text-right",
                item.to === activeItem
                  ? "text-foreground"
                  : "text-muted-foreground"
              )}
              onClick={() => setActiveItem(item.to)}
            >
              {item.title}
            </Link>
          ))}
        </div>
      </div>

      <div className="px-7 mt-auto">
        <Separator className="my-4" />
        <footer className="flex flex-col space-y-2 mb-4">
          {footerItems.map((link) => (
            <Link
              key={link.title}
              to={link.to}
              className="text-sm text-muted-foreground hover:text-foreground text-right"
            >
              {link.title}
            </Link>
          ))}
        </footer>

        <SignedOut>
          <div className="flex gap-1">
            <SignInButton>
              <Button
                variant={"default"}
                size={"sm"}
                className="px-2 min-w-[40px]"
              >
                Sign In
              </Button>
            </SignInButton>
          </div>
        </SignedOut>
      </div>
    </div>
  );
}
