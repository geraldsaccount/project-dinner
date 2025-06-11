import { Button } from "@/components/ui/button";
import {
  Tooltip,
  TooltipContent,
  TooltipProvider,
  TooltipTrigger,
} from "@/components/ui/tooltip";
import { Copy } from "lucide-react";
import { useState } from "react";

type Props = {
  inviteCode: string;
};

const InviteLinkSection = ({ inviteCode }: Props) => {
  const [copied, setCopied] = useState(false);
  const baseUrl = typeof window !== "undefined" ? window.location.origin : "";
  const inviteUrl = `${baseUrl}/invite/${inviteCode}`;

  const handleCopy = () => {
    navigator.clipboard.writeText(inviteUrl);
    setCopied(true);
    setTimeout(() => setCopied(false), 2000);
  };

  return (
    <div className="gap-1">
      <label className="block text-sm font-medium">
        Awaiting Guest - Share Invite:
      </label>
      <div className="flex items-center gap-2">
        <input
          id="invite-link"
          type="text"
          readOnly
          value={inviteUrl}
          className="flex-grow p-2 border border-muted-foreground rounded-md bg-gray-50 text-sm"
        />
        <TooltipProvider>
          <Tooltip open={copied || undefined} defaultOpen={false}>
            <TooltipTrigger asChild>
              <Button
                type="button"
                onClick={handleCopy}
                aria-label="Copy invitation link"
              >
                <Copy />
              </Button>
            </TooltipTrigger>
            <TooltipContent side="top">
              {copied ? (
                <span>Copied to clipboard!</span>
              ) : (
                "Copy invitation link"
              )}
            </TooltipContent>
          </Tooltip>
        </TooltipProvider>
      </div>
    </div>
  );
};

export default InviteLinkSection;
