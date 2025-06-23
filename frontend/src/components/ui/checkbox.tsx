import * as React from "react";
import * as CheckboxPrimitive from "@radix-ui/react-checkbox";
import { CheckIcon } from "lucide-react";

import { cn } from "@/lib/utils";

function Checkbox({
  className,
  ...props
}: React.ComponentProps<typeof CheckboxPrimitive.Root>) {
  return (
    <CheckboxPrimitive.Root
      data-slot="checkbox"
      className={cn(
        // Brutalist changes for Checkbox Root:
        // - Removed rounded-[4px] for sharp square
        // - Replaced subtle shadow with border
        // - Replaced soft ring focus with stark outline
        // - Using theme colors for background, border, checked state
        "peer bg-background text-foreground size-4 shrink-0 rounded-none border-2 border-border transition-none outline-none", // Brutalist base styling
        "data-[state=checked]:bg-primary data-[state=checked]:text-primary-foreground data-[state=checked]:border-primary", // Checked state: primary background/text/border
        "focus-visible:outline-2 focus-visible:outline-offset-0 focus-visible:outline-ring", // Stark focus outline
        "aria-invalid:border-destructive aria-invalid:outline-destructive", // Error state: destructive border/outline
        "disabled:cursor-not-allowed disabled:opacity-50", // Standard disabled state
        className
      )}
      {...props}
    >
      <CheckboxPrimitive.Indicator
        data-slot="checkbox-indicator"
        className="flex items-center justify-center text-current transition-none" // Kept text-current to inherit from parent, transition-none
      >
        <CheckIcon className="size-3.5" />
      </CheckboxPrimitive.Indicator>
    </CheckboxPrimitive.Root>
  );
}

export { Checkbox };
