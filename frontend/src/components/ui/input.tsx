import * as React from "react";

import { cn } from "@/lib/utils";

function Input({ className, type, ...props }: React.ComponentProps<"input">) {
  return (
    <input
      type={type}
      data-slot="input"
      className={cn(
        // Brutalist changes for Input:
        // - Removed rounded-md for sharp corners
        // - Removed shadow-xs for flat appearance
        // - Replaced soft border/ring focus with stark outline
        // - Using theme colors for background, border, placeholder, selection
        // - Removed transition for abrupt changes
        "file:text-foreground placeholder:text-muted-foreground selection:bg-primary selection:text-primary-foreground", // Standard text/placeholder/selection colors
        "bg-background text-foreground flex h-10 w-full min-w-0 rounded-none border-2 border-border px-3 py-1 text-base transition-none outline-none", // Brutalist base styling
        "file:inline-flex file:h-7 file:border-0 file:bg-transparent file:text-sm file:font-bold file:uppercase", // File input button styling
        "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
        "aria-invalid:border-destructive aria-invalid:outline-destructive", // Error state: destructive border/outline
        "disabled:pointer-events-none disabled:cursor-not-allowed disabled:opacity-50 md:text-sm", // Standard disabled state
        className
      )}
      {...props}
    />
  );
}

export { Input };
