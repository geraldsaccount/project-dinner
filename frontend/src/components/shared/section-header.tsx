import { cn } from "@/lib/utils";

type Props = {
  title: string;
  className?: string;
};

const SectionHeader = ({ title, className }: Props) => {
  return (
    <h3
      className={cn("text-2xl font-semibold text-muted-foreground", className)}
    >
      {title}
    </h3>
  );
};

export default SectionHeader;
