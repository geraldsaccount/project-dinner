import * as React from "react";

import { cn } from "@/lib/utils";

function Textarea({ className, ...props }: React.ComponentProps<"textarea">) {
  return (
    <textarea
      data-slot="textarea"
      className={cn(
        // Brutalist changes for Textarea:
        // - Removed rounded-md for sharp corners
        // - Removed shadow-xs for flat appearance
        // - Replaced subtle border/ring focus with stark outline
        // - Using theme colors for background, border, placeholder
        // - Removed transition for abrupt changes
        "placeholder:text-muted-foreground", // Standard placeholder color
        "bg-background text-foreground flex field-sizing-content min-h-24 w-full rounded-none border-2 border-border px-3 py-2 text-base transition-none outline-none", // Brutalist base styling, increased min-height
        "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
        "aria-invalid:border-destructive aria-invalid:outline-destructive", // Error state: destructive border/outline
        "disabled:cursor-not-allowed disabled:opacity-50 md:text-sm", // Standard disabled state
        className
      )}
      {...props}
    />
  );
}

export { Textarea };
