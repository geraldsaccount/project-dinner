type Props = {
  title: string;
};

const SectionHeader = ({ title }: Props) => {
  return (
    <h3 className="text-2xl font-semibold text-muted-foreground">{title}</h3>
  );
};

export default SectionHeader;
